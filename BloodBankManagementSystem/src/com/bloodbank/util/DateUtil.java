package com.bloodbank.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date operations
 */
public class DateUtil {
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
    /**
     * Convert string to LocalDate
     */
    public static LocalDate stringToDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch(DateTimeParseException e) {
            Logger.error("Invalid date format: " + dateStr);
            return null;
        }
    }
    
    /**
     * Convert LocalDate to string
     */
    public static String dateToString(LocalDate date) {
        if(date == null) return null;
        return date.format(formatter);
    }
    
    /**
     * Get current date as string
     */
    public static String getCurrentDateString() {
        return dateToString(LocalDate.now());
    }
    
    /**
     * Get current date as LocalDate
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Check if date is valid
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch(DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Get expiry date (35 days from now)
     */
    public static String getExpiryDate() {
        LocalDate expiry = LocalDate.now().plusDays(Constants.BLOOD_EXPIRY_DAYS);
        return dateToString(expiry);
    }
    
    /**
     * Check if date is expired
     */
    public static boolean isExpired(String dateStr) {
        try {
            LocalDate date = stringToDate(dateStr);
            return date != null && date.isBefore(LocalDate.now());
        } catch(Exception e) {
            Logger.error("Error checking expiry", e);
            return false;
        }
    }
}
