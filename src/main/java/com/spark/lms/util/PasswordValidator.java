package com.spark.lms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Utility class to validate password strength
 */
@Component
public class PasswordValidator {

    // Minimum password length
    private static final int MIN_LENGTH = 8;
    
    // Maximum password length
    private static final int MAX_LENGTH = 128;
    
    // Pattern for lowercase letters
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    
    // Pattern for uppercase letters
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    
    // Pattern for digits
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    
    // Pattern for special characters
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    
    /**
     * Validates password strength
     * 
     * @param password the password to validate
     * @return true if the password meets the strength requirements
     */
    public boolean isStrong(String password) {
        return getValidationErrors(password).isEmpty();
    }
    
    /**
     * Gets validation errors for a password
     * 
     * @param password the password to validate
     * @return list of validation error messages, empty if password is valid
     */
    public List<String> getValidationErrors(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password == null || password.isEmpty()) {
            errors.add("Password cannot be empty");
            return errors;
        }
        
        if (password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters long");
        }
        
        if (password.length() > MAX_LENGTH) {
            errors.add("Password cannot be longer than " + MAX_LENGTH + " characters");
        }
        
        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one lowercase letter");
        }
        
        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one uppercase letter");
        }
        
        if (!DIGIT_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one digit");
        }
        
        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            errors.add("Password must contain at least one special character");
        }
        
        return errors;
    }
    
    /**
     * Checks if a password is common or easily guessable
     * 
     * @param password the password to check
     * @return true if the password is common or easily guessable
     */
    public boolean isCommonPassword(String password) {
        // List of common passwords to check against
        String[] commonPasswords = {
            "password", "123456", "qwerty", "admin", "welcome",
            "password123", "admin123", "letmein", "monkey", "abc123",
            "111111", "12345678", "sunshine", "princess", "dragon",
            "trustno1", "welcome1", "football", "baseball", "superman"
        };
        
        if (password == null) {
            return false;
        }
        
        String lowerPassword = password.toLowerCase();
        
        for (String commonPassword : commonPasswords) {
            if (lowerPassword.equals(commonPassword)) {
                return true;
            }
        }
        
        return false;
    }
}
