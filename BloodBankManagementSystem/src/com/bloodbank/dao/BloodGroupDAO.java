package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.BloodGroup;
import java.sql.*;
import java.util.*;

public class BloodGroupDAO {
    
    /**
     * Get all blood groups
     */
    public List<BloodGroup> getAllBloodGroups() throws SQLException {
        List<BloodGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM blood_group ORDER BY blood_type";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            BloodGroup group = new BloodGroup();
            group.setGroupId(rs.getInt("group_id"));
            group.setBloodType(rs.getString("blood_type"));
            group.setAvailableUnits(rs.getInt("available_units"));
            groups.add(group);
        }
        
        rs.close();
        stmt.close();
        return groups;
    }

    /**
     * Get blood group by ID
     */
    public BloodGroup getBloodGroupById(int groupId) throws SQLException {
        String sql = "SELECT * FROM blood_group WHERE group_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, groupId);
        
        ResultSet rs = ps.executeQuery();
        BloodGroup group = null;
        
        if(rs.next()) {
            group = new BloodGroup();
            group.setGroupId(rs.getInt("group_id"));
            group.setBloodType(rs.getString("blood_type"));
            group.setAvailableUnits(rs.getInt("available_units"));
        }
        
        rs.close();
        ps.close();
        return group;
    }

    /**
     * Get blood group by blood type (NEW METHOD - THIS WAS MISSING)
     */
    public BloodGroup getBloodGroupByType(String bloodType) throws SQLException {
        String sql = "SELECT * FROM blood_group WHERE blood_type = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, bloodType);
        
        ResultSet rs = ps.executeQuery();
        BloodGroup group = null;
        
        if(rs.next()) {
            group = new BloodGroup();
            group.setGroupId(rs.getInt("group_id"));
            group.setBloodType(rs.getString("blood_type"));
            group.setAvailableUnits(rs.getInt("available_units"));
        }
        
        rs.close();
        ps.close();
        return group;
    }

    /**
     * Get available units for a blood type
     */
    public int getAvailableUnits(int groupId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM blood_unit WHERE group_id = ? AND status = 'Available' AND expiry_date > CURDATE()";
        
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, groupId);
        
        ResultSet rs = ps.executeQuery();
        int count = 0;
        
        if(rs.next()) {
            count = rs.getInt("count");
        }
        
        rs.close();
        ps.close();
        return count;
    }

    /**
     * Update available units for a blood group
     */
    public boolean updateAvailableUnits(int groupId, int units) throws SQLException {
        String sql = "UPDATE blood_group SET available_units = ? WHERE group_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setInt(1, units);
        ps.setInt(2, groupId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }
}
