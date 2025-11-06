package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.BloodUnit;
import java.sql.*;
import java.util.*;

public class BloodUnitDAO {
    
    public int addBloodUnit(BloodUnit unit) throws SQLException {
        String sql = "INSERT INTO blood_unit (group_id, volume_ml, collection_date, expiry_date, status) VALUES (?, ?, CURDATE(), ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setInt(1, unit.getGroupId());
        ps.setInt(2, unit.getVolumeMl());
        ps.setString(3, unit.getExpiryDate());
        ps.setString(4, unit.getStatus());
        
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public List<BloodUnit> getAllBloodUnits() throws SQLException {
        List<BloodUnit> units = new ArrayList<>();
        String sql = "SELECT bu.blood_unit_id, bu.group_id, bg.blood_type, bu.volume_ml, bu.collection_date, bu.expiry_date, bu.status, bu.created_at " +
                     "FROM blood_unit bu " +
                     "JOIN blood_group bg ON bu.group_id = bg.group_id " +
                     "ORDER BY bu.blood_unit_id DESC";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            BloodUnit unit = new BloodUnit();
            unit.setBloodUnitId(rs.getInt("blood_unit_id"));
            unit.setGroupId(rs.getInt("group_id"));
            unit.setBloodType(rs.getString("blood_type"));
            unit.setVolumeMl(rs.getInt("volume_ml"));
            unit.setCollectionDate(rs.getString("collection_date"));
            unit.setExpiryDate(rs.getString("expiry_date"));
            unit.setStatus(rs.getString("status"));
            units.add(unit);
        }
        
        rs.close();
        stmt.close();
        return units;
    }

    public List<BloodUnit> getAvailableBloodUnits() throws SQLException {
        List<BloodUnit> units = new ArrayList<>();
        String sql = "SELECT bu.blood_unit_id, bu.group_id, bg.blood_type, bu.volume_ml, bu.collection_date, bu.expiry_date, bu.status, bu.created_at " +
                     "FROM blood_unit bu " +
                     "JOIN blood_group bg ON bu.group_id = bg.group_id " +
                     "WHERE bu.status = 'Available' AND bu.expiry_date > CURDATE() " +
                     "ORDER BY bu.expiry_date ASC";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            BloodUnit unit = new BloodUnit();
            unit.setBloodUnitId(rs.getInt("blood_unit_id"));
            unit.setGroupId(rs.getInt("group_id"));
            unit.setBloodType(rs.getString("blood_type"));
            unit.setVolumeMl(rs.getInt("volume_ml"));
            unit.setCollectionDate(rs.getString("collection_date"));
            unit.setExpiryDate(rs.getString("expiry_date"));
            unit.setStatus(rs.getString("status"));
            units.add(unit);
        }
        
        rs.close();
        stmt.close();
        return units;
    }

    public boolean updateBloodUnitStatus(int unitId, String status) throws SQLException {
        String sql = "UPDATE blood_unit SET status = ? WHERE blood_unit_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, status);
        ps.setInt(2, unitId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    public List<Object[]> getInventorySummary() throws SQLException {
        List<Object[]> summary = new ArrayList<>();
        String sql = "SELECT bg.blood_type, " +
                     "COUNT(CASE WHEN bu.status = 'Available' AND bu.expiry_date > CURDATE() THEN 1 END) as available_units, " +
                     "COUNT(CASE WHEN bu.status = 'Reserved' THEN 1 END) as reserved_units " +
                     "FROM blood_group bg " +
                     "LEFT JOIN blood_unit bu ON bg.group_id = bu.group_id " +
                     "GROUP BY bg.group_id, bg.blood_type " +
                     "ORDER BY bg.blood_type";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
        	Object[] row = new Object[3];           // ✅ CORRECT - size 3
        	row[0] = rs.getString("blood_type");    // ✅ CORRECT - index 0
        	row[1] = rs.getInt("available_units");  // ✅ CORRECT - index 1
        	row[2] = rs.getInt("reserved_units");   // ✅ CORRECT - index 2
            summary.add(row);
        }
        
        rs.close();
        stmt.close();
        return summary;
    }
}
