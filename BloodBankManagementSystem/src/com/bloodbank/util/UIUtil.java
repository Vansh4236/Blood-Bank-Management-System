package com.bloodbank.util;

import javax.swing.*;

/**
 * Utility class for UI operations
 */
public class UIUtil {
    
    /**
     * Show success message dialog
     */
    public static void showSuccess(JComponent parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        Logger.success(message);
    }
    
    /**
     * Show error message dialog
     */
    public static void showError(JComponent parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
        Logger.error(message);
    }
    
    /**
     * Show warning message dialog
     */
    public static void showWarning(JComponent parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
        Logger.warning(message);
    }
    
    /**
     * Show info message dialog
     */
    public static void showInfo(JComponent parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        Logger.info(message);
    }
    
    /**
     * Show confirmation dialog
     */
    public static boolean showConfirmation(JComponent parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Show confirmation dialog with custom title
     */
    public static boolean showConfirmation(JComponent parent, String title, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
