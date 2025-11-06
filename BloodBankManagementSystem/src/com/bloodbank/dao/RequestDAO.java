package com.bloodbank.dao;

import com.bloodbank.database.DBConnection;
import com.bloodbank.models.Request;
import java.sql.*;
import java.util.*;

public class RequestDAO {
    
    public int addRequest(Request request) throws SQLException {
        String sql = "INSERT INTO request (patient_id, hospital_id, group_id, units_requested, request_date, status, urgency, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setInt(1, request.getPatientId());
        ps.setInt(2, request.getHospitalId());
        ps.setInt(3, request.getGroupId());
        ps.setInt(4, request.getUnitsRequested());
        ps.setString(5, request.getRequestDate());
        ps.setString(6, request.getStatus());
        ps.setString(7, request.getUrgency());
        ps.setString(8, request.getNotes());
        
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    public List<Request> getAllRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.*, p.name as patient_name, h.name as hospital_name, bg.blood_type FROM request r JOIN patient p ON r.patient_id = p.patient_id JOIN hospital h ON r.hospital_id = h.hospital_id JOIN blood_group bg ON r.group_id = bg.group_id";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            Request req = new Request();
            req.setRequestId(rs.getInt("request_id"));
            req.setPatientId(rs.getInt("patient_id"));
            req.setHospitalId(rs.getInt("hospital_id"));
            req.setGroupId(rs.getInt("group_id"));
            req.setUnitsRequested(rs.getInt("units_requested"));
            req.setRequestDate(rs.getString("request_date"));
            req.setStatus(rs.getString("status"));
            req.setUrgency(rs.getString("urgency"));
            req.setNotes(rs.getString("notes"));
            requests.add(req);
        }
        
        rs.close();
        stmt.close();
        return requests;
    }

    public List<Request> getPendingRequests() throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE status = 'Pending' ORDER BY urgency DESC";
        
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()) {
            Request req = new Request();
            req.setRequestId(rs.getInt("request_id"));
            req.setPatientId(rs.getInt("patient_id"));
            req.setHospitalId(rs.getInt("hospital_id"));
            req.setGroupId(rs.getInt("group_id"));
            req.setUnitsRequested(rs.getInt("units_requested"));
            req.setRequestDate(rs.getString("request_date"));
            req.setStatus(rs.getString("status"));
            req.setUrgency(rs.getString("urgency"));
            requests.add(req);
        }
        
        rs.close();
        stmt.close();
        return requests;
    }

    public boolean approveRequest(int requestId) throws SQLException {
        String sql = "UPDATE request SET status = 'Approved' WHERE request_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, requestId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    public boolean rejectRequest(int requestId) throws SQLException {
        String sql = "UPDATE request SET status = 'Rejected' WHERE request_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, requestId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }

    public boolean fulfillRequest(int requestId) throws SQLException {
        String sql = "UPDATE request SET status = 'Fulfilled', fulfilled_date = CURDATE() WHERE request_id = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, requestId);
        
        int result = ps.executeUpdate();
        ps.close();
        return result > 0;
    }
}
