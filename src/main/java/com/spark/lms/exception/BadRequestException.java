package com.spark.lms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the request is invalid or malformed.
 * This will trigger a 400 HTTP response.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new bad request exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructs a new bad request exception with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
