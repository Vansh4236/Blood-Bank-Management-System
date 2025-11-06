package com.bloodbank.util;

public class Constants {
    // Application Info
    public static final String APP_TITLE = "Blood Bank Management System";
    public static final String APP_VERSION = "1.0.0";
    
    // Age constraints
    public static final int MIN_DONOR_AGE = 18;
    public static final int MAX_DONOR_AGE = 65;
    public static final int MIN_PATIENT_AGE = 1;
    public static final int MAX_PATIENT_AGE = 120;
    
    // Blood specifications
    public static final int STANDARD_BLOOD_VOLUME = 450;
    public static final int BLOOD_EXPIRY_DAYS = 35;
    
    // Error Messages
    public static final String ERROR_INVALID_AGE = "Age must be between required range";
    public static final String ERROR_INVALID_EMAIL = "Invalid email format";
    public static final String ERROR_INVALID_CONTACT = "Contact must be 10 digits";
    public static final String ERROR_DATABASE_CONNECTION = "Failed to connect to database";
    public static final String ERROR_EMPTY_FIELD = "Cannot be empty";
    public static final String ERROR_INVALID_INPUT = "Invalid input";
    public static final String ERROR_DATABASE_OPERATION = "Database operation failed";
    
    // Success Messages
    public static final String SUCCESS_MESSAGE = "Operation successful!";
    public static final String SUCCESS_ADDED = "Record added successfully!";
    public static final String SUCCESS_UPDATED = "Record updated successfully!";
    public static final String SUCCESS_DELETED = "Record deleted successfully!";
    public static final String SUCCESS_APPROVED = "Record approved successfully!";
    public static final String SUCCESS_REJECTED = "Record rejected successfully!";
    
    // Confirmation Messages
    public static final String CONFIRM_DELETE = "Are you sure you want to delete this record?";
    public static final String CONFIRM_ACTION = "Are you sure?";
    
    // Blood Types
    public static final String[] BLOOD_TYPES = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    
    // Gender Options
    public static final String[] GENDERS = {"Male", "Female", "Other"};
    
    // Status Options
    public static final String[] DONATION_STATUS = {"Pending", "Approved", "Rejected"};
    public static final String[] BLOOD_UNIT_STATUS = {"Available", "Reserved", "Used", "Expired"};
    public static final String[] REQUEST_STATUS = {"Pending", "Approved", "Rejected", "Fulfilled"};
    public static final String[] URGENCY_LEVELS = {"Low", "Medium", "High", "Critical"};
}
