package com.bloodbank.view;

import com.bloodbank.dao.DonationDAO;
import com.bloodbank.dao.DonorDAO;
import com.bloodbank.dao.StaffDAO;
import com.bloodbank.dao.BloodUnitDAO;
import com.bloodbank.models.Donation;
import com.bloodbank.models.Donor;
import com.bloodbank.models.Staff;
import com.bloodbank.models.BloodUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DonationPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DonationDAO donationDAO;
    private DonorDAO donorDAO;
    private StaffDAO staffDAO;
    private BloodUnitDAO bloodUnitDAO;
    private JTable donationTable;
    private DefaultTableModel tableModel;

    public DonationPanel() {
        donationDAO = new DonationDAO();
        donorDAO = new DonorDAO();
        staffDAO = new StaffDAO();
        bloodUnitDAO = new BloodUnitDAO();
        initComponents();
        loadDonations();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Add Donation");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadDonations());
        
        JButton approveBtn = new JButton("Approve");
        approveBtn.addActionListener(e -> approveDonation());
        
        JButton rejectBtn = new JButton("Reject");
        rejectBtn.addActionListener(e -> rejectDonation());
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(approveBtn);
        topPanel.add(rejectBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Donor ID", "Staff ID", "Blood Unit", "Date", "Hb Level", "BP", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        donationTable = new JTable(tableModel);
        donationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(donationTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDonations() {
        tableModel.setRowCount(0);
        try {
            List<Donation> donations = donationDAO.getAllDonations();
            for(Donation d : donations) {
                Object[] row = {d.getDonationId(), d.getDonorId(), d.getStaffId(), d.getBloodUnitId(), 
                               d.getDonationDate(), d.getHemoglobinLevel(), d.getBloodPressure(), d.getStatus()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Donation");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<Donor> donorBox = new JComboBox<>();
        JComboBox<Staff> staffBox = new JComboBox<>();
        JComboBox<BloodUnit> bloodUnitBox = new JComboBox<>();
        JTextField dateField = new JTextField();
        JTextField hbField = new JTextField();
        JTextField bpField = new JTextField();
        JTextField notesField = new JTextField();

        try {
            List<Donor> donors = donorDAO.getAllDonors();
            for(Donor d : donors) {
                donorBox.addItem(d);
            }
            List<Staff> staff = staffDAO.getAllStaff();
            for(Staff s : staff) {
                staffBox.addItem(s);
            }
            List<BloodUnit> units = bloodUnitDAO.getAvailableBloodUnits();
            for(BloodUnit u : units) {
                bloodUnitBox.addItem(u);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        panel.add(new JLabel("Donor:")); panel.add(donorBox);
        panel.add(new JLabel("Staff:")); panel.add(staffBox);
        panel.add(new JLabel("Blood Unit:")); panel.add(bloodUnitBox);
        panel.add(new JLabel("Date (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Hemoglobin Level:")); panel.add(hbField);
        panel.add(new JLabel("Blood Pressure:")); panel.add(bpField);
        panel.add(new JLabel("Notes:")); panel.add(notesField);

        JButton saveBtn = new JButton("Add");
        saveBtn.addActionListener(e -> {
            try {
                Donor donor = (Donor) donorBox.getSelectedItem();
                Staff staff = (Staff) staffBox.getSelectedItem();
                BloodUnit unit = (BloodUnit) bloodUnitBox.getSelectedItem();
                
                Donation donation = new Donation(donor.getDonorId(), staff.getStaffId(), unit.getBloodUnitId(), dateField.getText());
                donation.setHemoglobinLevel(hbField.getText());
                donation.setBloodPressure(bpField.getText());
                donation.setNotes(notesField.getText());
                
                donationDAO.addDonation(donation);
                JOptionPane.showMessageDialog(dialog, "Donation added!");
                dialog.dispose();
                loadDonations();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void approveDonation() {
        int selectedRow = donationTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a donation");
            return;
        }

        int donationId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            donationDAO.approveDonation(donationId);
            JOptionPane.showMessageDialog(this, "Donation approved!");
            loadDonations();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void rejectDonation() {
        int selectedRow = donationTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a donation");
            return;
        }

        int donationId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            donationDAO.rejectDonation(donationId);
            JOptionPane.showMessageDialog(this, "Donation rejected!");
            loadDonations();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
