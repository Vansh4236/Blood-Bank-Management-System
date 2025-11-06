package com.bloodbank.view;

import com.bloodbank.dao.PatientDAO;
import com.bloodbank.models.Patient;
import com.bloodbank.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PatientPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PatientDAO patientDAO;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public PatientPanel() {
        patientDAO = new PatientDAO();
        initComponents();
        loadPatients();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JButton addBtn = new JButton("Add Patient");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadPatients());
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deletePatient());
        
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchPatients());
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(deleteBtn);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Age", "Gender", "Contact", "Disease", "Blood Required"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for(Patient p : patients) {
                Object[] row = {p.getPatientId(), p.getName(), p.getAge(), p.getGender(), 
                               p.getContact(), p.getDisease(), p.getBloodGroupRequired()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchPatients() {
        String term = searchField.getText();
        if(term.isEmpty()) {
            loadPatients();
            return;
        }
        tableModel.setRowCount(0);
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for(Patient p : patients) {
                if(p.getName().toLowerCase().contains(term.toLowerCase()) || 
                   p.getContact().contains(term)) {
                    Object[] row = {p.getPatientId(), p.getName(), p.getAge(), p.getGender(), 
                                   p.getContact(), p.getDisease(), p.getBloodGroupRequired()};
                    tableModel.addRow(row);
                }
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Patient");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField contactField = new JTextField();
        JTextField diseaseField = new JTextField();
        JComboBox<String> bloodGroupBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(new JLabel("Gender:")); panel.add(genderBox);
        panel.add(new JLabel("Contact:")); panel.add(contactField);
        panel.add(new JLabel("Disease:")); panel.add(diseaseField);
        panel.add(new JLabel("Blood Required:")); panel.add(bloodGroupBox);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String gender = (String) genderBox.getSelectedItem();
                String contact = contactField.getText();
                String disease = diseaseField.getText();
                String bloodGroup = (String) bloodGroupBox.getSelectedItem();

                if(!Validator.isNotEmpty(name)) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty");
                    return;
                }
                if(!Validator.isValidPhone(contact)) {
                    JOptionPane.showMessageDialog(dialog, "Contact must be 10 digits");
                    return;
                }

                Patient patient = new Patient(name, age, gender, contact, disease, bloodGroup);
                patientDAO.addPatient(patient);
                JOptionPane.showMessageDialog(dialog, "Patient added successfully!");
                dialog.dispose();
                loadPatients();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete");
            return;
        }

        int patientId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this patient?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION) {
            try {
                patientDAO.deletePatient(patientId);
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                loadPatients();
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
