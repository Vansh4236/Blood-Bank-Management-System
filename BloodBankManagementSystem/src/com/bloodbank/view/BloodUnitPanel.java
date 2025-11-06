package com.bloodbank.view;

import com.bloodbank.dao.BloodGroupDAO;
import com.bloodbank.dao.BloodUnitDAO;
import com.bloodbank.models.BloodGroup;
import com.bloodbank.models.BloodUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BloodUnitPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BloodUnitDAO bloodUnitDAO;
    private BloodGroupDAO bloodGroupDAO;
    private JTable bloodUnitTable;
    private DefaultTableModel tableModel;

    public BloodUnitPanel() {
        bloodUnitDAO = new BloodUnitDAO();
        bloodGroupDAO = new BloodGroupDAO();
        initComponents();
        loadBloodUnits();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addBtn = new JButton("Add Blood Unit");
        addBtn.addActionListener(e -> openAddDialog());
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadBloodUnits());
        
        JButton markUsedBtn = new JButton("Mark as Used");
        markUsedBtn.addActionListener(e -> updateStatus("Used"));
        
        topPanel.add(addBtn);
        topPanel.add(refreshBtn);
        topPanel.add(markUsedBtn);
        
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Unit ID", "Blood Type", "Volume (ml)", "Collection Date", "Expiry Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        bloodUnitTable = new JTable(tableModel);
        bloodUnitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(bloodUnitTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadBloodUnits() {
        tableModel.setRowCount(0);
        try {
            List<BloodUnit> units = bloodUnitDAO.getAllBloodUnits();
            for(BloodUnit u : units) {
                Object[] row = {u.getBloodUnitId(), u.getBloodType(), u.getVolumeMl(), 
                               u.getCollectionDate(), u.getExpiryDate(), u.getStatus()};
                tableModel.addRow(row);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Blood Unit");
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<BloodGroup> bloodGroupBox = new JComboBox<>();
        JTextField volumeField = new JTextField("450");
        JTextField expiryField = new JTextField("2025-11-30");

        try {
            List<BloodGroup> groups = bloodGroupDAO.getAllBloodGroups();
            for(BloodGroup g : groups) {
                bloodGroupBox.addItem(g);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        panel.add(new JLabel("Blood Group:")); panel.add(bloodGroupBox);
        panel.add(new JLabel("Volume (ml):")); panel.add(volumeField);
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):")); panel.add(expiryField);

        JButton saveBtn = new JButton("Add");
        saveBtn.addActionListener(e -> {
            try {
                BloodGroup bg = (BloodGroup) bloodGroupBox.getSelectedItem();
                int volume = Integer.parseInt(volumeField.getText());
                String expiryDate = expiryField.getText();

                BloodUnit unit = new BloodUnit(bg.getGroupId(), volume, expiryDate);
                bloodUnitDAO.addBloodUnit(unit);
                JOptionPane.showMessageDialog(dialog, "Blood unit added!");
                dialog.dispose();
                loadBloodUnits();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid volume format");
            }
        });

        panel.add(saveBtn);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void updateStatus(String status) {
        int selectedRow = bloodUnitTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a blood unit");
            return;
        }

        int unitId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            bloodUnitDAO.updateBloodUnitStatus(unitId, status);
            JOptionPane.showMessageDialog(this, "Status updated!");
            loadBloodUnits();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
