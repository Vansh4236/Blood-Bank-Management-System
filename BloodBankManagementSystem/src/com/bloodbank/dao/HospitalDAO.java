package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.Hospital;
import java.sql.*;
import java.util.*;

public class HospitalDAO {
    
    public int addHospital(Hospital hospital) throws SQLException {
        String sql = "INSERT INTO hospital (name, address, contact_no, email, city) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, hospital.getName());
        ps.setString(2, hospital.getAddress());
        ps.setString(3, hospital.getContactNo());
        ps.setString(4, hospital.getEmail());
        ps.setString(5, hospital.getCity());
        
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public List<Hospital> getAllHospitals() throws SQLException {
        List<Hospital> hospitals = new ArrayList<>();
        String sql = "SELECT * FROM hospital";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            Hospital hospital = new Hospital();
            hospital.setHospitalId(rs.getInt("hospital_id"));
            hospital.setName(rs.getString("name"));
            hospital.setAddress(rs.getString("address"));
            hospital.setContactNo(rs.getString("contact_no"));
            hospital.setEmail(rs.getString("email"));
            hospital.setCity(rs.getString("city"));
            hospitals.add(hospital);
        }
        
        rs.close();
        stmt.close();
        return hospitals;
    }

    public boolean updateHospital(Hospital hospital) throws SQLException {
        String sql = "UPDATE hospital SET name=?, address=?, contact_no=?, email=?, city=? WHERE hospital_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, hospital.getName());
        ps.setString(2, hospital.getAddress());
        ps.setString(3, hospital.getContactNo());
        ps.setString(4, hospital.getEmail());
        ps.setString(5, hospital.getCity());
        ps.setInt(6, hospital.getHospitalId());
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    public boolean deleteHospital(int hospitalId) throws SQLException {
        String sql = "DELETE FROM hospital WHERE hospital_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, hospitalId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }
}
