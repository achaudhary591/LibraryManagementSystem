package com.spark.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

/**
 * Controller for handling error pages
 */
@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    /**
     * Handle general errors
     */
    @GetMapping("/general-error")
    public String handleGeneralError(HttpServletRequest request, Model model) {
        // Add error details to the model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "An unexpected error occurred");
        model.addAttribute("url", request.getRequestURL());
        
        return "error/general-error";
    }

    /**
     * Handle not found errors
     */
    @GetMapping("/not-found")
    public String handleNotFound(HttpServletRequest request, Model model) {
        // Add error details to the model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "The requested resource could not be found");
        model.addAttribute("url", request.getRequestURL());
        
        return "error/not-found";
    }

    /**
     * Handle access denied errors
     */
    @GetMapping("/access-denied")
    public String handleAccessDenied(HttpServletRequest request, Model model) {
        // Add error details to the model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "You do not have permission to access this resource");
        model.addAttribute("url", request.getRequestURL());
        
        return "error/access-denied";
    }

    /**
     * Handle bad request errors
     */
    @GetMapping("/bad-request")
    public String handleBadRequest(HttpServletRequest request, Model model) {
        // Add error details to the model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "The request could not be processed due to invalid parameters");
        model.addAttribute("url", request.getRequestURL());
        
        return "error/bad-request";
    }

    /**
     * Handle database errors
     */
    @GetMapping("/database-error")
    public String handleDatabaseError(HttpServletRequest request, Model model) {
        // Add error details to the model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "A database error occurred");
        model.addAttribute("url", request.getRequestURL());
        
        return "error/database-error";
    }

    /**
     * Handle validation errors
     */
    @GetMapping("/validation-error")
    public String handleValidationError(HttpServletRequest request, Model model) {
        // Add error details to the model
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("message", "The submitted data contains validation errors");
        model.addAttribute("url", request.getRequestURL());
        
        return "error/validation-error";
    }
}
