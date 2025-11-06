package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.Donation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {

    // Create a new donation row; pass null for blood_unit_id if you want it auto-created on approval
    public int addDonation(Donation donation) throws SQLException {
        String sql = "INSERT INTO donation " +
                "(donor_id, staff_id, blood_unit_id, donation_date, hemoglobin_level, blood_pressure, status, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donation.getDonorId());
            ps.setInt(2, donation.getStaffId());

            // Allow null blood_unit_id; UI may leave it null until approval creates the unit
            if (donation.getBloodUnitId() > 0) {
                ps.setInt(3, donation.getBloodUnitId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, donation.getDonationDate());
            ps.setString(5, donation.getHemoglobinLevel());
            ps.setString(6, donation.getBloodPressure());
            ps.setString(7, donation.getStatus());
            ps.setString(8, donation.getNotes());
            return ps.executeUpdate();
        }
    }

    // Join through blood_unit -> blood_group to get blood_type (no bu.blood_type direct reference)
    public List<Donation> getAllDonations() throws SQLException {
        List<Donation> donations = new ArrayList<>();
        String sql =
            "SELECT d.donation_id, d.donor_id, d.staff_id, d.blood_unit_id, d.donation_date, " +
            "       d.hemoglobin_level, d.blood_pressure, d.status, d.notes, " +
            "       d2.name AS donor_name, s.name AS staff_name, bg.blood_type " +
            "FROM donation d " +
            "JOIN donor d2 ON d.donor_id = d2.donor_id " +
            "JOIN staff s ON d.staff_id = s.staff_id " +
            "LEFT JOIN blood_unit bu ON d.blood_unit_id = bu.blood_unit_id " +
            "LEFT JOIN blood_group bg ON bu.group_id = bg.group_id " +
            "ORDER BY d.donation_id DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Donation donation = new Donation();
                donation.setDonationId(rs.getInt("donation_id"));
                donation.setDonorId(rs.getInt("donor_id"));
                donation.setStaffId(rs.getInt("staff_id"));
                donation.setBloodUnitId(rs.getInt("blood_unit_id"));
                donation.setDonationDate(rs.getString("donation_date"));
                donation.setHemoglobinLevel(rs.getString("hemoglobin_level"));
                donation.setBloodPressure(rs.getString("blood_pressure"));
                donation.setStatus(rs.getString("status"));
                donation.setNotes(rs.getString("notes"));
                // If needed in UI, read rs.getString("blood_type") here and store in a view DTO
                donations.add(donation);
            }
        }
        return donations;
    }

    public List<Donation> getPendingDonations() throws SQLException {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donation WHERE status = 'Pending' ORDER BY donation_id DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Donation donation = new Donation();
                donation.setDonationId(rs.getInt("donation_id"));
                donation.setDonorId(rs.getInt("donor_id"));
                donation.setStaffId(rs.getInt("staff_id"));
                donation.setBloodUnitId(rs.getInt("blood_unit_id"));
                donation.setDonationDate(rs.getString("donation_date"));
                donation.setHemoglobinLevel(rs.getString("hemoglobin_level"));
                donation.setBloodPressure(rs.getString("blood_pressure"));
                donation.setStatus(rs.getString("status"));
                donation.setNotes(rs.getString("notes"));
                donations.add(donation);
            }
        }
        return donations;
    }

    public boolean updateDonationStatus(int donationId, String status) throws SQLException {
        String sql = "UPDATE donation SET status = ? WHERE donation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, donationId);
            return ps.executeUpdate() > 0;
        }
    }

    // Approve donation and ensure inventory reflects a created/available blood unit
    public boolean approveDonation(int donationId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        boolean oldAuto = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            // 1) Mark donation Approved
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE donation SET status='Approved' WHERE donation_id=?")) {
                ps.setInt(1, donationId);
                ps.executeUpdate();
            }

            // 2) Get donor_id and existing blood_unit_id
            int donorId;
            Integer bloodUnitId = null;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT donor_id, blood_unit_id FROM donation WHERE donation_id=? FOR UPDATE")) {
                ps.setInt(1, donationId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Donation not found: " + donationId);
                    donorId = rs.getInt("donor_id");
                    int bu = rs.getInt("blood_unit_id");
                    bloodUnitId = rs.wasNull() ? null : bu;
                }
            }

            // 3) Resolve donor's blood group_id
            int groupId;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT blood_group_id FROM donor WHERE donor_id=?")) {
                ps.setInt(1, donorId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Donor not found for donation: " + donationId);
                    groupId = rs.getInt("blood_group_id");
                }
            }

            // 4) If no blood_unit linked yet, create one and link it
            if (bloodUnitId == null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO blood_unit (group_id, volume_ml, collection_date, expiry_date, status) " +
                        "VALUES (?, 450, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY), 'Available')",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, groupId);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("Failed to create blood unit for donation: " + donationId);
                        bloodUnitId = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE donation SET blood_unit_id=? WHERE donation_id=?")) {
                    ps.setInt(1, bloodUnitId);
                    ps.setInt(2, donationId);
                    ps.executeUpdate();
                }
            } else {
                // 5) If unit already exists, ensure it's marked Available and dated
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE blood_unit SET status='Available', " +
                        "collection_date=COALESCE(collection_date, CURDATE()), " +
                        "expiry_date=COALESCE(expiry_date, DATE_ADD(CURDATE(), INTERVAL 35 DAY)) " +
                        "WHERE blood_unit_id=?")) {
                    ps.setInt(1, bloodUnitId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(oldAuto);
            return true;
        } catch (SQLException e) {
            conn.rollback();
            conn.setAutoCommit(oldAuto);
            throw e;
        }
    }

    public boolean rejectDonation(int donationId) throws SQLException {
        return updateDonationStatus(donationId, "Rejected");
    }
}
