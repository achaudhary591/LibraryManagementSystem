package com.spark.lms.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for timing database operations
 * This helps identify slow queries for optimization
 */
@Aspect
@Component
public class DatabaseQueryTimer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseQueryTimer.class);
    
    /**
     * Times repository method executions
     */
    @Around("execution(* com.spark.lms.repository.*.*(..))")
    public Object timeRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeMethod(joinPoint, "Repository");
    }
    
    /**
     * Times service method executions that are likely to involve database operations
     */
    @Around("execution(* com.spark.lms.service.*.find*(..)) || " +
            "execution(* com.spark.lms.service.*.get*(..)) || " +
            "execution(* com.spark.lms.service.*.save*(..)) || " +
            "execution(* com.spark.lms.service.*.update*(..)) || " +
            "execution(* com.spark.lms.service.*.delete*(..))")
    public Object timeServiceDatabaseMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return timeMethod(joinPoint, "Service");
    }
    
    /**
     * Generic method to time method execution and log if it exceeds thresholds
     */
    private Object timeMethod(ProceedingJoinPoint joinPoint, String type) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        
        // Log all queries for debugging
        logger.debug("{} method executed: {} - {} ms", type, methodName, executionTime);
        
        // Log slow queries with warning level
        if (executionTime > 500) {
            logger.warn("Slow {} method detected: {} - {} ms", type, methodName, executionTime);
        }
        
        return result;
    }
}
