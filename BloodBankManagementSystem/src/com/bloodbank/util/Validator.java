package com.bloodbank.util;

import com.bloodbank.exception.ValidationException;

public class Validator {
    
    /**
     * Validate if email is in valid format
     */
    public static boolean isValidEmail(String email) {
        if(email == null || email.isEmpty()) {
            return true; // Email is optional
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Validate if phone number is 10 digits
     */
    public static boolean isValidPhone(String phone) {
        if(phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("[0-9]{10}");
    }
    
    /**
     * Validate if string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate donor age
     */
    public static boolean isValidDonorAge(int age) {
        return age >= Constants.MIN_DONOR_AGE && age <= Constants.MAX_DONOR_AGE;
    }
    
    /**
     * Validate patient age
     */
    public static boolean isValidPatientAge(int age) {
        return age >= Constants.MIN_PATIENT_AGE && age <= Constants.MAX_PATIENT_AGE;
    }
    
    /**
     * Validate age
     */
    public static boolean isValidAge(int age, int minAge, int maxAge) {
        return age >= minAge && age <= maxAge;
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }
    
    /**
     * Validate email with exception
     */
    public static void validateEmail(String email) throws ValidationException {
        if(!isValidEmail(email)) {
            throw new ValidationException(Constants.ERROR_INVALID_EMAIL);
        }
    }
    
    /**
     * Validate phone with exception
     */
    public static void validatePhone(String phone) throws ValidationException {
        if(!isValidPhone(phone)) {
            throw new ValidationException(Constants.ERROR_INVALID_CONTACT);
        }
    }
    
    /**
     * Validate not empty with exception
     */
    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if(!isNotEmpty(value)) {
            throw new ValidationException(fieldName + " " + Constants.ERROR_EMPTY_FIELD);
        }
    }
    
    /**
     * Validate donor age with exception
     */
    public static void validateDonorAge(int age) throws ValidationException {
        if(!isValidDonorAge(age)) {
            throw new ValidationException("Donor age must be between " + 
                Constants.MIN_DONOR_AGE + " and " + Constants.MAX_DONOR_AGE);
        }
    }
    
    /**
     * Validate patient age with exception
     */
    public static void validatePatientAge(int age) throws ValidationException {
        if(!isValidPatientAge(age)) {
            throw new ValidationException("Patient age must be between " + 
                Constants.MIN_PATIENT_AGE + " and " + Constants.MAX_PATIENT_AGE);
        }
    }
    
    /**
     * Validate positive with exception
     */
    public static void validatePositive(int value, String fieldName) throws ValidationException {
        if(!isPositive(value)) {
            throw new ValidationException(fieldName + " must be positive");
        }
    }
}
