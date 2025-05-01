package com.spark.lms.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for monitoring request performance
 */
public class PerformanceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceInterceptor.class);
    private static final String REQUEST_START_TIME = "requestStartTime";
    
    // Threshold in milliseconds for slow request warning
    private static final long SLOW_REQUEST_THRESHOLD_MS = 500;

    /**
     * Pre-handle method to record the start time of the request
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
        return true;
    }

    /**
     * Post-handle method to calculate and log request processing time
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // No action needed here
    }

    /**
     * After-completion method to log request processing time
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            String path = request.getRequestURI();
            String method = request.getMethod();
            int status = response.getStatus();
            
            // Log all requests at debug level
            logger.debug("Request: {} {} - {} ms, status: {}", method, path, executionTime, status);
            
            // Log slow requests at warning level
            if (executionTime > SLOW_REQUEST_THRESHOLD_MS) {
                logger.warn("Slow request detected: {} {} - {} ms, status: {}", method, path, executionTime, status);
            }
        }
    }
}
