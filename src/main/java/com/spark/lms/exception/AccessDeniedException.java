package com.spark.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to access a resource they don't have permission for.
 * This will trigger a 403 HTTP response.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new access denied exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public AccessDeniedException(String message) {
        super(message);
    }

    /**
     * Constructs a new access denied exception with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
