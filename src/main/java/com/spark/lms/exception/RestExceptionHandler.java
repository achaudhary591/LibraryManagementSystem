package com.spark.lms.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception handler specifically for REST controllers.
 * This provides consistent error responses for the REST API.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "com.spark.lms.controller.rest")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = "Malformed JSON request";
        logger.error(error, ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.setDebugMessage(ex.getLocalizedMessage());
        
        // Add all field errors
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            apiError.addValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
        });
        
        logger.error("Validation error", ex);
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle MissingServletRequestParameterException. Triggered when a required request parameter is missing.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        logger.error(error, ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }
    
    /**
     * Handle NoHandlerFoundException. Triggered when the requested URL doesn't match any handler.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
        apiError.setDebugMessage(ex.getMessage());
        logger.error("No handler found", ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        logger.error("Entity not found", ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        logger.error("Resource not found: {}", ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle BadRequestException
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        logger.error("Bad request: {}", ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationException(ValidationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setValidationErrors(ex.getErrors());
        logger.error("Validation error: {}", ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage("Access denied: " + ex.getMessage());
        logger.error("Access denied: {}", ex.getMessage());
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle Spring Security authentication exceptions
     */
    @ExceptionHandler({BadCredentialsException.class, DisabledException.class, LockedException.class})
    protected ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        
        if (ex instanceof BadCredentialsException) {
            apiError.setMessage("Invalid username or password");
        } else if (ex instanceof DisabledException) {
            apiError.setMessage("Account is disabled");
        } else if (ex instanceof LockedException) {
            apiError.setMessage("Account is locked");
        } else {
            apiError.setMessage("Authentication failed");
        }
        
        apiError.setDebugMessage(ex.getMessage());
        logger.error("Authentication error: {}", ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle DataIntegrityViolationException, happens when database constraint is violated
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setMessage("Database error");
        
        if (ex.getCause() instanceof ConstraintViolationException) {
            apiError.setMessage("Database constraint violation");
            apiError.setDebugMessage(extractConstraintMessage(ex.getMessage()));
        } else {
            apiError.setDebugMessage(ex.getMessage());
        }
        
        logger.error("Data integrity violation", ex);
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle ConstraintViolationException, happens when @Validated fails
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        
        ex.getConstraintViolations().forEach(violation -> {
            apiError.addValidationError(
                violation.getRootBeanClass().getSimpleName(),
                violation.getPropertyPath().toString(),
                violation.getInvalidValue(),
                violation.getMessage());
        });
        
        logger.error("Constraint violation", ex);
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle MethodArgumentTypeMismatchException, happens when method argument is not the expected type
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        logger.error("Method argument type mismatch", ex);
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage("Unexpected error occurred");
        apiError.setDebugMessage(ex.getMessage());
        logger.error("Unexpected error", ex);
        return buildResponseEntity(apiError);
    }

    /**
     * Extract a user-friendly message from constraint violation errors
     */
    private String extractConstraintMessage(String message) {
        if (message == null) {
            return "Unknown constraint violation";
        }
        
        if (message.contains("fkhogjvyf6hvjcihbd4erumh663")) {
            return "Cannot delete member because it is associated with a user account";
        }
        
        if (message.contains("foreign key constraint")) {
            return "This record cannot be deleted because it is referenced by other records";
        }
        
        if (message.contains("unique constraint") || message.contains("Duplicate entry")) {
            return "A record with the same unique identifier already exists";
        }
        
        if (message.contains("not-null constraint")) {
            return "Required fields cannot be empty";
        }
        
        return "Database constraint violation";
    }

    /**
     * Build a ResponseEntity with an ApiError
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
