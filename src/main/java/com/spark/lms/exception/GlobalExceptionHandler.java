package com.spark.lms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for the application
 * Provides centralized exception handling across all @RequestMapping methods
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle data integrity violations (e.g., foreign key constraint violations)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolation(HttpServletRequest request, DataIntegrityViolationException ex) {
        logger.error("Data integrity violation: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "Database constraint violation: " + extractConstraintMessage(ex.getMessage()));
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/database-error");
        
        return mav;
    }
    
    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(HttpServletRequest request, ResourceNotFoundException ex) {
        logger.error("Resource not found: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", ex.getMessage());
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/not-found");
        
        return mav;
    }
    
    /**
     * Handle EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFound(HttpServletRequest request, EntityNotFoundException ex) {
        logger.error("Entity not found: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", ex.getMessage());
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/not-found");
        
        return mav;
    }
    
    /**
     * Handle NoResourceFoundException (404 errors)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFound(HttpServletRequest request, NoResourceFoundException ex) {
        logger.error("Resource not found: {}", request.getRequestURL(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "The requested resource could not be found");
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/not-found");
        
        return mav;
    }
    
    /**
     * Handle BadRequestException
     */
    @ExceptionHandler(BadRequestException.class)
    public ModelAndView handleBadRequest(HttpServletRequest request, BadRequestException ex) {
        logger.error("Bad request: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", ex.getMessage());
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/bad-request");
        
        return mav;
    }
    
    /**
     * Handle ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ModelAndView handleValidationException(HttpServletRequest request, ValidationException ex) {
        logger.error("Validation error: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("errors", ex.getErrors());
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", ex.getMessage());
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/validation-error");
        
        return mav;
    }
    
    /**
     * Handle BindException (form validation errors)
     */
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(HttpServletRequest request, BindException ex) {
        logger.error("Form validation error: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "Form validation error");
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.addObject("errors", ex.getBindingResult().getAllErrors());
        mav.setViewName("error/validation-error");
        
        return mav;
    }
    
    /**
     * Handle ConstraintViolationException (Bean validation errors)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolation(HttpServletRequest request, ConstraintViolationException ex) {
        logger.error("Constraint violation: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "Validation error");
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.addObject("violations", ex.getConstraintViolations());
        mav.setViewName("error/validation-error");
        
        return mav;
    }
    
    /**
     * Handle AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(HttpServletRequest request, AccessDeniedException ex) {
        logger.error("Access denied: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "You do not have permission to access this resource");
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/access-denied");
        
        return mav;
    }
    
    /**
     * Handle MaxUploadSizeExceededException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceeded(HttpServletRequest request, MaxUploadSizeExceededException ex) {
        logger.error("File upload size exceeded: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "The uploaded file exceeds the maximum allowed size");
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/bad-request");
        
        return mav;
    }
    
    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(HttpServletRequest request, Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("message", "An unexpected error occurred: " + ex.getMessage());
        mav.addObject("timestamp", java.time.LocalDateTime.now());
        mav.setViewName("error/general-error");
        
        return mav;
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
        
        if (message.contains("unique constraint") || message.contains("duplicate key")) {
            return "A record with the same unique identifier already exists";
        }
        
        if (message.contains("not-null constraint")) {
            return "Required fields cannot be empty";
        }
        
        return "Database constraint violation";
    }
}
