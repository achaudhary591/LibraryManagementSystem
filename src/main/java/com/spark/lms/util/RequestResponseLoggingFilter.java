package com.spark.lms.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for logging HTTP requests and responses
 * Adds a request ID to each request for traceability
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_MDC = "requestId";
    
    // List of headers that should not be logged due to sensitivity
    private static final Collection<String> SENSITIVE_HEADERS = 
            Collections.unmodifiableCollection(Collections.singletonList("Authorization"));
    
    // List of content types that should have their bodies logged
    private static final Collection<String> LOGGABLE_CONTENT_TYPES = 
            Collections.unmodifiableCollection(Collections.singletonList("application/json"));
    
    // Maximum content length to log
    private static final int MAX_CONTENT_LENGTH = 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Check if request should be logged
        if (shouldLog(request)) {
            doFilterWithLogging(request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }
    
    /**
     * Filter with request/response logging
     */
    private void doFilterWithLogging(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate or get request ID
        String requestId = request.getHeader(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        
        // Add request ID to MDC for logging
        MDC.put(REQUEST_ID_MDC, requestId);
        
        // Wrap request and response for content caching
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        // Add request ID to response headers
        responseWrapper.setHeader(REQUEST_ID_HEADER, requestId);
        
        try {
            // Log request
            logRequest(requestWrapper, requestId);
            
            // Process request
            filterChain.doFilter(requestWrapper, responseWrapper);
            
            // Log response
            logResponse(responseWrapper, requestId);
        } finally {
            // Copy content to the original response
            responseWrapper.copyBodyToResponse();
            
            // Remove request ID from MDC
            MDC.remove(REQUEST_ID_MDC);
        }
    }
    
    /**
     * Log request details
     */
    private void logRequest(ContentCachingRequestWrapper request, String requestId) {
        // Log request line
        logger.info("Request: [{}] {} {} {}", 
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getProtocol());
        
        // Log request headers
        Map<String, String> headers = getHeaders(request);
        logger.debug("Request headers: [{}] {}", requestId, headers);
        
        // Log request parameters
        Map<String, String[]> parameters = request.getParameterMap();
        if (!parameters.isEmpty()) {
            logger.debug("Request parameters: [{}] {}", requestId, parameters);
        }
    }
    
    /**
     * Log response details
     */
    private void logResponse(ContentCachingResponseWrapper response, String requestId) {
        // Log response status
        logger.info("Response: [{}] {}", requestId, response.getStatus());
        
        // Log response headers
        Map<String, String> headers = getHeaders(response);
        logger.debug("Response headers: [{}] {}", requestId, headers);
        
        // Log response content for specific content types
        String contentType = response.getContentType();
        if (contentType != null && isLoggableContentType(contentType)) {
            byte[] content = response.getContentAsByteArray();
            if (content.length > 0) {
                String contentAsString = new String(content).substring(0, Math.min(content.length, MAX_CONTENT_LENGTH));
                if (content.length > MAX_CONTENT_LENGTH) {
                    contentAsString += "... (truncated)";
                }
                logger.debug("Response body: [{}] {}", requestId, contentAsString);
            }
        }
    }
    
    /**
     * Get headers as a map, excluding sensitive headers
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!SENSITIVE_HEADERS.contains(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            } else {
                headers.put(headerName, "******");
            }
        }
        
        return headers;
    }
    
    /**
     * Get headers as a map, excluding sensitive headers
     */
    private Map<String, String> getHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        
        for (String headerName : headerNames) {
            if (!SENSITIVE_HEADERS.contains(headerName)) {
                headers.put(headerName, response.getHeader(headerName));
            } else {
                headers.put(headerName, "******");
            }
        }
        
        return headers;
    }
    
    /**
     * Check if the content type should be logged
     */
    private boolean isLoggableContentType(String contentType) {
        return LOGGABLE_CONTENT_TYPES.stream()
                .anyMatch(contentType.toLowerCase()::contains);
    }
    
    /**
     * Determine if the request should be logged
     */
    private boolean shouldLog(HttpServletRequest request) {
        // Skip logging for static resources
        String path = request.getRequestURI();
        return !path.contains("/static/") && 
               !path.contains("/css/") && 
               !path.contains("/js/") && 
               !path.contains("/images/") &&
               !path.contains("/fonts/");
    }
}
