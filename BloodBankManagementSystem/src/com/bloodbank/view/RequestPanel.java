package com.bloodbank.view;

import com.bloodbank.dao.RequestDAO;
import com.bloodbank.dao.PatientDAO;
import com.bloodbank.dao.HospitalDAO;
import com.bloodbank.dao.BloodGroupDAO;
import com.bloodbank.models.Request;
import com.bloodbank.models.Patient;
import com.bloodbank.models.Hospital;
import com.bloodbank.models.BloodGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RequestPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestDAO requestDAO;
    private PatientDAO patientDAO;
    private HospitalDAO hospitalDAO;
    private BloodGroupDAO bloodGroupDAO;
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public RequestPanel() {
        requestDAO = new RequestDAO();
        patientDAO = new PatientDAO();
        hospitalDAO = new HospitalDAO();
        bloodGroupDAO = new BloodGroupDAO();
        initComponents();
        loadRequests();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Add Request");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadRequests());
        
        JButton approveBtn = new JButton("Approve");
        approveBtn.addActionListener(e -> approveRequest());
        
        JButton fulfillBtn = new JButton("Fulfill");
        fulfillBtn.addActionListener(e -> fulfillRequest());
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(approveBtn);
        topPanel.add(fulfillBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Patient", "Hospital", "Blood Type", "Units", "Date", "Status", "Urgency"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        requestTable = new JTable(tableModel);
        requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRequests() {
        tableModel.setRowCount(0);
        try {
            List<Request> requests = requestDAO.getAllRequests();
            for(Request r : requests) {
                Object[] row = {r.getRequestId(), r.getPatientId(), r.getHospitalId(), r.getGroupId(), 
                               r.getUnitsRequested(), r.getRequestDate(), r.getStatus(), r.getUrgency()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Request");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Patient> patientBox = new JComboBox<>();
        JComboBox<Hospital> hospitalBox = new JComboBox<>();
        JComboBox<BloodGroup> bloodGroupBox = new JComboBox<>();
        JTextField unitsField = new JTextField();
        JTextField dateField = new JTextField();
        JComboBox<String> urgencyBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});

        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for(Patient p : patients) {
                patientBox.addItem(p);
            }
            List<Hospital> hospitals = hospitalDAO.getAllHospitals();
            for(Hospital h : hospitals) {
                hospitalBox.addItem(h);
            }
            List<BloodGroup> groups = bloodGroupDAO.getAllBloodGroups();
            for(BloodGroup g : groups) {
                bloodGroupBox.addItem(g);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        panel.add(new JLabel("Patient:")); panel.add(patientBox);
        panel.add(new JLabel("Hospital:")); panel.add(hospitalBox);
        panel.add(new JLabel("Blood Group:")); panel.add(bloodGroupBox);
        panel.add(new JLabel("Units Requested:")); panel.add(unitsField);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Urgency:")); panel.add(urgencyBox);

        JButton saveBtn = new JButton("Add");
        saveBtn.addActionListener(e -> {
            try {
                Patient patient = (Patient) patientBox.getSelectedItem();
                Hospital hospital = (Hospital) hospitalBox.getSelectedItem();
                BloodGroup group = (BloodGroup) bloodGroupBox.getSelectedItem();
                
                Request request = new Request(patient.getPatientId(), hospital.getHospitalId(), 
                                            group.getGroupId(), Integer.parseInt(unitsField.getText()), dateField.getText());
                request.setUrgency((String) urgencyBox.getSelectedItem());
                
                requestDAO.addRequest(request);
                JOptionPane.showMessageDialog(dialog, "Request added!");
                dialog.dispose();
                loadRequests();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void approveRequest() {
        int selectedRow = requestTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request");
            return;
        }

        int requestId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            requestDAO.approveRequest(requestId);
            JOptionPane.showMessageDialog(this, "Request approved!");
            loadRequests();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void fulfillRequest() {
        int selectedRow = requestTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request");
            return;
        }

        int requestId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            requestDAO.fulfillRequest(requestId);
            JOptionPane.showMessageDialog(this, "Request fulfilled!");
            loadRequests();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
