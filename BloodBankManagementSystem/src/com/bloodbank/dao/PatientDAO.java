package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.Patient;
import java.sql.*;
import java.util.*;

public class PatientDAO {
    
    public int addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patient (name, age, gender, contact, disease, blood_group_required) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, patient.getName());
        ps.setInt(2, patient.getAge());
        ps.setString(3, patient.getGender());
        ps.setString(4, patient.getContact());
        ps.setString(5, patient.getDisease());
        ps.setString(6, patient.getBloodGroupRequired());
        
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            Patient patient = new Patient();
            patient.setPatientId(rs.getInt("patient_id"));
            patient.setName(rs.getString("name"));
            patient.setAge(rs.getInt("age"));
            patient.setGender(rs.getString("gender"));
            patient.setContact(rs.getString("contact"));
            patient.setDisease(rs.getString("disease"));
            patient.setBloodGroupRequired(rs.getString("blood_group_required"));
            patients.add(patient);
        }
        
        rs.close();
        stmt.close();
        return patients;
    }

    public Patient getPatientById(int patientId) throws SQLException {
        String sql = "SELECT * FROM patient WHERE patient_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, patientId);
        
        ResultSet rs = ps.executeQuery();
        Patient patient = null;
        
        if(rs.next()) {
            patient = new Patient();
            patient.setPatientId(rs.getInt("patient_id"));
            patient.setName(rs.getString("name"));
            patient.setAge(rs.getInt("age"));
            patient.setGender(rs.getString("gender"));
            patient.setContact(rs.getString("contact"));
            patient.setDisease(rs.getString("disease"));
            patient.setBloodGroupRequired(rs.getString("blood_group_required"));
        }
        
        rs.close();
        ps.close();
        return patient;
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patient SET name=?, age=?, gender=?, contact=?, disease=?, blood_group_required=? WHERE patient_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setString(1, patient.getName());
        ps.setInt(2, patient.getAge());
        ps.setString(3, patient.getGender());
        ps.setString(4, patient.getContact());
        ps.setString(5, patient.getDisease());
        ps.setString(6, patient.getBloodGroupRequired());
        ps.setInt(7, patient.getPatientId());
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    public boolean deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM patient WHERE patient_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, patientId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }
}
