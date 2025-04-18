package com.spark.lms.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor for logging HTTP requests and responses
 */
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    /**
     * Log request details before handler execution
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request: {} {} from {}", 
                request.getMethod(), 
                request.getRequestURI(),
                request.getRemoteAddr());
        
        return true;
    }

    /**
     * Log after handler execution but before view rendering
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        logger.debug("Post handle: {} {}, status: {}", 
                request.getMethod(), 
                request.getRequestURI(),
                response.getStatus());
    }

    /**
     * Log after request completion
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null) {
            logger.error("Exception during request processing: {} {}", 
                    request.getMethod(), 
                    request.getRequestURI(), 
                    ex);
        } else {
            logger.info("Completed: {} {}, status: {}", 
                    request.getMethod(), 
                    request.getRequestURI(),
                    response.getStatus());
        }
    }
}
