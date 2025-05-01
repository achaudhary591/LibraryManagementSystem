package com.spark.lms.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception thrown when validation errors occur
 */
public class ValidationException extends RuntimeException {
    
    private final Map<String, String> errors = new HashMap<>();
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        if (errors != null) {
            this.errors.putAll(errors);
        }
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
    public void addError(String field, String message) {
        errors.put(field, message);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
