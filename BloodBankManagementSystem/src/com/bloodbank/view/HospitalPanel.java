package com.bloodbank.view;

import com.bloodbank.dao.HospitalDAO;
import com.bloodbank.models.Hospital;
import com.bloodbank.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class HospitalPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HospitalDAO hospitalDAO;
    private JTable hospitalTable;
    private DefaultTableModel tableModel;

    public HospitalPanel() {
        hospitalDAO = new HospitalDAO();
        initComponents();
        loadHospitals();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Add Hospital");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadHospitals());
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> deleteHospital());
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(deleteBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Address", "Contact", "Email", "City"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        hospitalTable = new JTable(tableModel);
        hospitalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(hospitalTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadHospitals() {
        tableModel.setRowCount(0);
        try {
            List<Hospital> hospitals = hospitalDAO.getAllHospitals();
            for(Hospital h : hospitals) {
                Object[] row = {h.getHospitalId(), h.getName(), h.getAddress(), 
                               h.getContactNo(), h.getEmail(), h.getCity()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Hospital");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField cityField = new JTextField();

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Contact:")); panel.add(contactField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("City:")); panel.add(cityField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                if(!Validator.isNotEmpty(nameField.getText())) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty");
                    return;
                }

                Hospital h = new Hospital(nameField.getText(), addressField.getText(), 
                                        contactField.getText(), emailField.getText(), cityField.getText());
                hospitalDAO.addHospital(h);
                JOptionPane.showMessageDialog(dialog, "Hospital added!");
                dialog.dispose();
                loadHospitals();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a hospital to delete");
            return;
        }

        int hospitalId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this hospital?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if(confirm == JOptionPane.YES_OPTION) {
            try {
                hospitalDAO.deleteHospital(hospitalId);
                JOptionPane.showMessageDialog(this, "Hospital deleted!");
                loadHospitals();
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
