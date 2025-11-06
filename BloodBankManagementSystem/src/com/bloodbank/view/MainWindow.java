package com.bloodbank.view;

import com.bloodbank.util.Constants;
import javax.swing.*;

public class MainWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;

    public MainWindow() {
        setTitle(Constants.APP_TITLE + " v" + Constants.APP_VERSION);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed interface
        tabbedPane = new JTabbedPane();

        // Add all tabs - make sure all panel classes are created
        tabbedPane.addTab("Dashboard", new InventoryPanel());
        tabbedPane.addTab("Donors", new DonorPanel());
        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Blood Units", new BloodUnitPanel());
        tabbedPane.addTab("Donations", new DonationPanel());
        tabbedPane.addTab("Hospitals", new HospitalPanel());
        tabbedPane.addTab("Staff", new StaffPanel());
        tabbedPane.addTab("Requests", new RequestPanel());

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
