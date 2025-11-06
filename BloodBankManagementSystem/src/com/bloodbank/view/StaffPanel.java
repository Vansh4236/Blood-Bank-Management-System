package com.bloodbank.view;

import com.bloodbank.dao.StaffDAO;
import com.bloodbank.models.Staff;
import com.bloodbank.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StaffPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private StaffDAO staffDAO;
    private JTable staffTable;
    private DefaultTableModel tableModel;

    public StaffPanel() {
        staffDAO = new StaffDAO();
        initComponents();
        loadStaff();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Add Staff");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadStaff());
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deleteStaff());
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(deleteBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Role", "Contact", "Shift Time", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(staffTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadStaff() {
        tableModel.setRowCount(0);
        try {
            List<Staff> staffList = staffDAO.getAllStaff();
            for(Staff s : staffList) {
                Object[] row = {s.getStaffId(), s.getName(), s.getRole(), 
                               s.getContactNo(), s.getShiftTime(), s.getEmail()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Staff");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField shiftField = new JTextField();
        JTextField emailField = new JTextField();

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Role:")); panel.add(roleField);
        panel.add(new JLabel("Contact:")); panel.add(contactField);
        panel.add(new JLabel("Shift Time:")); panel.add(shiftField);
        panel.add(new JLabel("Email:")); panel.add(emailField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                if(!Validator.isNotEmpty(nameField.getText())) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty");
                    return;
                }

                Staff staff = new Staff(nameField.getText(), roleField.getText(), 
                                      contactField.getText(), shiftField.getText(), emailField.getText());
                staffDAO.addStaff(staff);
                JOptionPane.showMessageDialog(dialog, "Staff added!");
                dialog.dispose();
                loadStaff();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select staff to delete");
            return;
        }

        int staffId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this staff?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION) {
            try {
                staffDAO.deleteStaff(staffId);
                JOptionPane.showMessageDialog(this, "Staff deleted!");
                loadStaff();
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
