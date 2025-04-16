package com.spark.lms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

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
        
        return "Database constraint violation";
    }
}
