package com.bloodbank.view;

import com.bloodbank.dao.BloodUnitDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class InventoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final BloodUnitDAO bloodUnitDAO = new BloodUnitDAO();
    private final JPanel grid = new JPanel(new GridLayout(2, 4, 10, 10));

    public InventoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refresh());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(refreshBtn);
        add(top, BorderLayout.NORTH);

        add(grid, BorderLayout.CENTER);

        // Initial load
        refresh();
    }

    // Call this to reload counts from DB (also call it when Dashboard tab is selected)
    public void refresh() {
        grid.removeAll();

        // Ensure all 8 types render even if DB has zero rows
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String bt : bloodTypes) {
            grid.add(createCard(bt, 0, 0));
        }

        try {
            // Each row: [blood_type, available_units, reserved_units]
            List<Object[]> summary = bloodUnitDAO.getInventorySummary();
            // Replace placeholder cards with real values
            for (Object[] row : summary) {
                String bloodType = (String) row[0];
                int available = row[1] == null ? 0 : (Integer) row[1];
                int reserved  = row[2] == null ? 0 : (Integer) row[2];

                // Find and replace the placeholder card for this bloodType
                replaceCardForType(bloodType, available, reserved);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        revalidate();
        repaint();
    }

    private void replaceCardForType(String bloodType, int available, int reserved) {
        // Remove the first card with matching title and add updated one
        for (int i = 0; i < grid.getComponentCount(); i++) {
            Component c = grid.getComponent(i);
            if (c instanceof JPanel panel) {
                // First child label holds the type text
                if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JLabel lbl) {
                    if (bloodType.equals(lbl.getText())) {
                        grid.remove(i);
                        grid.add(createCard(bloodType, available, reserved), i);
                        return;
                    }
                }
            }
        }
    }

    private JPanel createCard(String bloodType, int available, int reserved) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setBackground(colorFor(available));

        JLabel typeLabel = new JLabel(bloodType);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel availLabel = new JLabel("Available: " + available);
        availLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel reservedLabel = new JLabel("Reserved: " + reserved);
        reservedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(typeLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(availLabel);
        card.add(reservedLabel);
        return card;
    }

    private Color colorFor(int available) {
        if (available == 0) return new Color(255, 210, 210);
        if (available < 5) return new Color(255, 255, 200);
        return new Color(210, 255, 210);
    }
}
