package com.bloodbank.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging utility for application
 */
public class Logger {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Log info message
     */
    public static void info(String message) {
        System.out.println("[INFO] [" + LocalDateTime.now().format(formatter) + "] " + message);
    }
    
    /**
     * Log error message
     */
    public static void error(String message) {
        System.err.println("[ERROR] [" + LocalDateTime.now().format(formatter) + "] " + message);
    }
    
    /**
     * Log error with exception
     */
    public static void error(String message, Throwable throwable) {
        System.err.println("[ERROR] [" + LocalDateTime.now().format(formatter) + "] " + message);
        throwable.printStackTrace();
    }
    
    /**
     * Log debug message
     */
    public static void debug(String message) {
        System.out.println("[DEBUG] [" + LocalDateTime.now().format(formatter) + "] " + message);
    }
    
    /**
     * Log warning message
     */
    public static void warning(String message) {
        System.out.println("[WARNING] [" + LocalDateTime.now().format(formatter) + "] " + message);
    }
    
    /**
     * Log success message
     */
    public static void success(String message) {
        System.out.println("[SUCCESS] [" + LocalDateTime.now().format(formatter) + "] " + message);
    }
}
