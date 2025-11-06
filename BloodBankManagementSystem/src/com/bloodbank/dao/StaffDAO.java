package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.Staff;
import java.sql.*;
import java.util.*;

public class StaffDAO {
    
    public int addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (name, role, contact_no, shift_time, email) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, staff.getName());
        ps.setString(2, staff.getRole());
        ps.setString(3, staff.getContactNo());
        ps.setString(4, staff.getShiftTime());
        ps.setString(5, staff.getEmail());
        
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            Staff staff = new Staff();
            staff.setStaffId(rs.getInt("staff_id"));
            staff.setName(rs.getString("name"));
            staff.setRole(rs.getString("role"));
            staff.setContactNo(rs.getString("contact_no"));
            staff.setShiftTime(rs.getString("shift_time"));
            staff.setEmail(rs.getString("email"));
            staffList.add(staff);
        }
        
        rs.close();
        stmt.close();
        return staffList;
    }

    public boolean updateStaff(Staff staff) throws SQLException {
        String sql = "UPDATE staff SET name=?, role=?, contact_no=?, shift_time=?, email=? WHERE staff_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, staff.getName());
        ps.setString(2, staff.getRole());
        ps.setString(3, staff.getContactNo());
        ps.setString(4, staff.getShiftTime());
        ps.setString(5, staff.getEmail());
        ps.setInt(6, staff.getStaffId());
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    public boolean deleteStaff(int staffId) throws SQLException {
        String sql = "DELETE FROM staff WHERE staff_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, staffId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }
}
