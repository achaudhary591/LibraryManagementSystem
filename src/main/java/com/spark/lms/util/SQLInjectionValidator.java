package com.spark.lms.util;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class to validate input against SQL injection attempts
 */
@Component
public class SQLInjectionValidator {

    private static final Logger logger = LoggerFactory.getLogger(SQLInjectionValidator.class);
    
    // Pattern to detect common SQL injection attempts
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "(?i).*('|;|--|/\\*|\\*/|xp_|exec|execute|insert|select|delete|update|drop|alter|create|" +
            "union\\s+select|union\\s+all\\s+select|concat\\s*\\(|concat_ws\\s*\\(|group_concat|load_file|outfile).*");
    
    /**
     * Validates if the input contains potential SQL injection attempts
     * 
     * @param input the input string to validate
     * @return true if the input is safe, false if it might contain SQL injection
     */
    public boolean isSafe(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        
        boolean isSafe = !SQL_INJECTION_PATTERN.matcher(input).matches();
        
        if (!isSafe) {
            logger.warn("Potential SQL injection attempt detected: {}", input);
        }
        
        return isSafe;
    }
    
    /**
     * Sanitizes input by removing potentially dangerous SQL characters
     * Note: This is a basic sanitization and should not be relied upon as the only defense
     * 
     * @param input the input string to sanitize
     * @return sanitized string
     */
    public String sanitize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        // Replace dangerous characters
        String sanitized = input.replaceAll("['\"\\\\;]", "");
        
        if (!input.equals(sanitized)) {
            logger.info("Input sanitized: {} -> {}", input, sanitized);
        }
        
        return sanitized;
    }
    
    /**
     * Validates and throws exception if input is potentially dangerous
     * 
     * @param input the input string to validate
     * @param fieldName the name of the field being validated (for error message)
     * @throws IllegalArgumentException if the input might contain SQL injection
     */
    public void validateOrThrow(String input, String fieldName) {
        if (!isSafe(input)) {
            String message = "Potentially malicious input detected in field: " + fieldName;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}
