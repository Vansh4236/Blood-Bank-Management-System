package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.Donor;
import java.sql.*;
import java.util.*;

public class DonorDAO {
    
    // Add new donor
    public int addDonor(Donor donor) throws SQLException {
        String sql = "INSERT INTO donor (name, age, gender, contact, blood_group_id, email, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, donor.getName());
        ps.setInt(2, donor.getAge());
        ps.setString(3, donor.getGender());
        ps.setString(4, donor.getContact());
        ps.setInt(5, donor.getBloodGroupId());
        ps.setString(6, donor.getEmail());
        ps.setString(7, donor.getAddress());
        
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    // Get all donors
    public List<Donor> getAllDonors() throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT d.*, bg.blood_type FROM donor d JOIN blood_group bg ON d.blood_group_id = bg.group_id";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            Donor donor = new Donor();
            donor.setDonorId(rs.getInt("donor_id"));
            donor.setName(rs.getString("name"));
            donor.setAge(rs.getInt("age"));
            donor.setGender(rs.getString("gender"));
            donor.setContact(rs.getString("contact"));
            donor.setBloodGroupId(rs.getInt("blood_group_id"));
            donor.setBloodType(rs.getString("blood_type"));
            donor.setEmail(rs.getString("email"));
            donor.setAddress(rs.getString("address"));
            donor.setLastDonationDate(rs.getString("last_donation_date"));
            donors.add(donor);
        }
        
        rs.close();
        stmt.close();
        return donors;
    }

    // Get donor by ID
    public Donor getDonorById(int donorId) throws SQLException {
        String sql = "SELECT d.*, bg.blood_type FROM donor d JOIN blood_group bg ON d.blood_group_id = bg.group_id WHERE d.donor_id = ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, donorId);
        
        ResultSet rs = ps.executeQuery();
        Donor donor = null;
        
        if(rs.next()) {
            donor = new Donor();
            donor.setDonorId(rs.getInt("donor_id"));
            donor.setName(rs.getString("name"));
            donor.setAge(rs.getInt("age"));
            donor.setGender(rs.getString("gender"));
            donor.setContact(rs.getString("contact"));
            donor.setBloodGroupId(rs.getInt("blood_group_id"));
            donor.setBloodType(rs.getString("blood_type"));
            donor.setEmail(rs.getString("email"));
            donor.setAddress(rs.getString("address"));
        }
        
        rs.close();
        ps.close();
        return donor;
    }

    // Update donor
    public boolean updateDonor(Donor donor) throws SQLException {
        String sql = "UPDATE donor SET name=?, age=?, gender=?, contact=?, blood_group_id=?, email=?, address=? WHERE donor_id=?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, donor.getName());
        ps.setInt(2, donor.getAge());
        ps.setString(3, donor.getGender());
        ps.setString(4, donor.getContact());
        ps.setInt(5, donor.getBloodGroupId());
        ps.setString(6, donor.getEmail());
        ps.setString(7, donor.getAddress());
        ps.setInt(8, donor.getDonorId());
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    // Delete donor
    public boolean deleteDonor(int donorId) throws SQLException {
        String sql = "DELETE FROM donor WHERE donor_id=?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, donorId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    // Search donors
    public List<Donor> searchDonors(String searchTerm) throws SQLException {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT d.*, bg.blood_type FROM donor d JOIN blood_group bg ON d.blood_group_id = bg.group_id WHERE d.name LIKE ? OR d.contact LIKE ?";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        String pattern = "%" + searchTerm + "%";
        ps.setString(1, pattern);
        ps.setString(2, pattern);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Donor donor = new Donor();
            donor.setDonorId(rs.getInt("donor_id"));
            donor.setName(rs.getString("name"));
            donor.setAge(rs.getInt("age"));
            donor.setGender(rs.getString("gender"));
            donor.setContact(rs.getString("contact"));
            donor.setBloodGroupId(rs.getInt("blood_group_id"));
            donor.setBloodType(rs.getString("blood_type"));
            donor.setEmail(rs.getString("email"));
            donor.setAddress(rs.getString("address"));
            donors.add(donor);
        }
        
        rs.close();
        ps.close();
        return donors;
    }
}
