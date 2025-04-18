package com.spark.lms.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Standard error response object for REST API errors.
 * This provides a consistent error response format across the API.
 */
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    /**
     * Default constructor
     */
    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with HTTP status
     * 
     * @param status the HTTP status
     */
    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    /**
     * Constructor with HTTP status and exception
     * 
     * @param status the HTTP status
     * @param ex the exception
     */
    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Constructor with HTTP status, message and exception
     * 
     * @param status the HTTP status
     * @param message the error message
     * @param ex the exception
     */
    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Add a validation error to the sub-errors list
     * 
     * @param object the object that failed validation
     * @param field the field that failed validation
     * @param rejectedValue the invalid value
     * @param message the validation error message
     */
    public void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    /**
     * Add a sub-error to the list
     * 
     * @param subError the sub-error to add
     */
    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    // Getters and setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiSubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiSubError> subErrors) {
        this.subErrors = subErrors;
    }

    /**
     * Abstract class for API sub-errors
     */
    abstract static class ApiSubError {
    }

    /**
     * Class for validation errors
     */
    static class ApiValidationError extends ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        ApiValidationError(String object, String field, Object rejectedValue, String message) {
            this.object = object;
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        // Getters and setters

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
