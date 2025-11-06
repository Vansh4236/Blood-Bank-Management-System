package com.bloodbank.view;

import com.bloodbank.dao.BloodGroupDAO;
import com.bloodbank.dao.DonorDAO;
import com.bloodbank.models.BloodGroup;
import com.bloodbank.models.Donor;
import com.bloodbank.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DonorPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DonorDAO donorDAO;
    private BloodGroupDAO bloodGroupDAO;
    private JTable donorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public DonorPanel() {
        donorDAO = new DonorDAO();
        bloodGroupDAO = new BloodGroupDAO();
        initComponents();
        loadDonors();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JButton addBtn = new JButton("Add Donor");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadDonors());
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deleteDonor());
        
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchDonors());
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(deleteBtn);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Age", "Gender", "Contact", "Blood Type", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        donorTable = new JTable(tableModel);
        donorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(donorTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDonors() {
        tableModel.setRowCount(0);
        try {
            List<Donor> donors = donorDAO.getAllDonors();
            for(Donor d : donors) {
                Object[] row = {d.getDonorId(), d.getName(), d.getAge(), d.getGender(), 
                               d.getContact(), d.getBloodType(), d.getEmail()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading donors: " + e.getMessage());
        }
    }

    private void searchDonors() {
        String term = searchField.getText();
        if(term.isEmpty()) {
            loadDonors();
            return;
        }
        
        tableModel.setRowCount(0);
        try {
            List<Donor> donors = donorDAO.searchDonors(term);
            for(Donor d : donors) {
                Object[] row = {d.getDonorId(), d.getName(), d.getAge(), d.getGender(), 
                               d.getContact(), d.getBloodType(), d.getEmail()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Donor");
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField contactField = new JTextField();
        
        // Use String array for blood types instead of database
        JComboBox<String> bloodGroupBox = new JComboBox<>(new String[]{
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        });
        
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(new JLabel("Gender:")); panel.add(genderBox);
        panel.add(new JLabel("Contact:")); panel.add(contactField);
        panel.add(new JLabel("Blood Group:")); panel.add(bloodGroupBox);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Address:")); panel.add(addressField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText());
                String gender = (String) genderBox.getSelectedItem();
                String contact = contactField.getText().trim();
                String bloodTypeStr = (String) bloodGroupBox.getSelectedItem();
                String email = emailField.getText().trim();
                String address = addressField.getText().trim();

                if(!Validator.isNotEmpty(name)) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty");
                    return;
                }
                if(!Validator.isValidDonorAge(age)) {
                    JOptionPane.showMessageDialog(dialog, "Age must be between 18 and 65");
                    return;
                }
                if(!Validator.isValidPhone(contact)) {
                    JOptionPane.showMessageDialog(dialog, "Contact must be 10 digits");
                    return;
                }

                // Get blood group ID from database
                BloodGroup bg = bloodGroupDAO.getBloodGroupByType(bloodTypeStr);
                if(bg == null) {
                    JOptionPane.showMessageDialog(dialog, "Blood group not found");
                    return;
                }

                Donor donor = new Donor(name, age, gender, contact, bg.getGroupId(), email, address);
                donorDAO.addDonor(donor);
                JOptionPane.showMessageDialog(dialog, "Donor added successfully!");
                dialog.dispose();
                loadDonors();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid age format");
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a donor to delete");
            return;
        }

        int donorId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this donor?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION) {
            try {
                donorDAO.deleteDonor(donorId);
                JOptionPane.showMessageDialog(this, "Donor deleted successfully!");
                loadDonors();
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
