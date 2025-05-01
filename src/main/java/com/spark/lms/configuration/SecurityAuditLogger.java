package com.spark.lms.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Enhanced component to log security-related events with more detailed information
 */
@Component
public class SecurityAuditLogger {

    private static final Logger securityLogger = LoggerFactory.getLogger("org.springframework.security");
    private static final Logger auditLogger = LoggerFactory.getLogger("security-audit");

    /**
     * Log authentication success events
     */
    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        String ipAddress = extractIpAddress(auth);
        
        securityLogger.info("Login successful for user: {}, authorities: {}, IP: {}", 
                auth.getName(), 
                auth.getAuthorities(), 
                ipAddress);
                
        auditLogger.info("LOGIN_SUCCESS user='{}', roles={}, ip={}, session={}", 
                auth.getName(),
                auth.getAuthorities(),
                ipAddress,
                extractSessionId(auth));
    }

    /**
     * Log authentication failure events - bad credentials
     */
    @EventListener
    public void authenticationFailureBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        Authentication auth = event.getAuthentication();
        String ipAddress = extractIpAddress(auth);
        
        securityLogger.warn("Login failed for user: {}, reason: bad credentials, IP: {}", 
                auth.getName(), 
                ipAddress);
                
        auditLogger.warn("LOGIN_FAILURE user='{}', reason='bad_credentials', ip={}", 
                auth.getName(),
                ipAddress);
    }
    
    /**
     * Log authentication failure events - account disabled
     */
    @EventListener
    public void authenticationFailureDisabled(AuthenticationFailureDisabledEvent event) {
        Authentication auth = event.getAuthentication();
        String ipAddress = extractIpAddress(auth);
        
        securityLogger.warn("Login failed for user: {}, reason: account disabled, IP: {}", 
                auth.getName(), 
                ipAddress);
                
        auditLogger.warn("LOGIN_FAILURE user='{}', reason='account_disabled', ip={}", 
                auth.getName(),
                ipAddress);
    }
    
    /**
     * Log authentication failure events - account locked
     */
    @EventListener
    public void authenticationFailureLocked(AuthenticationFailureLockedEvent event) {
        Authentication auth = event.getAuthentication();
        String ipAddress = extractIpAddress(auth);
        
        securityLogger.warn("Login failed for user: {}, reason: account locked, IP: {}", 
                auth.getName(), 
                ipAddress);
                
        auditLogger.warn("LOGIN_FAILURE user='{}', reason='account_locked', ip={}", 
                auth.getName(),
                ipAddress);
    }
    
    /**
     * Log authentication failure events - account expired
     */
    @EventListener
    public void authenticationFailureExpired(AuthenticationFailureExpiredEvent event) {
        Authentication auth = event.getAuthentication();
        String ipAddress = extractIpAddress(auth);
        
        securityLogger.warn("Login failed for user: {}, reason: account expired, IP: {}", 
                auth.getName(), 
                ipAddress);
                
        auditLogger.warn("LOGIN_FAILURE user='{}', reason='account_expired', ip={}", 
                auth.getName(),
                ipAddress);
    }

    /**
     * Log logout success events
     */
    @EventListener
    public void logoutSuccess(LogoutSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        String ipAddress = extractIpAddress(auth);
        
        securityLogger.info("Logout successful for user: {}, IP: {}", 
                auth.getName(),
                ipAddress);
                
        auditLogger.info("LOGOUT user='{}', ip={}, session={}", 
                auth.getName(),
                ipAddress,
                extractSessionId(auth));
    }
    
    /**
     * Log authorization failure events
     * Note: We're not using the deprecated AuthorizationFailureEvent anymore
     */
    public void logAuthorizationFailure(Authentication auth, String resource) {
        if (auth != null) {
            String ipAddress = extractIpAddress(auth);
            
            securityLogger.warn("Access denied for user: {}, resource: {}, IP: {}", 
                    auth.getName(),
                    resource,
                    ipAddress);
                    
            auditLogger.warn("ACCESS_DENIED user='{}', resource='{}', ip={}", 
                    auth.getName(),
                    resource,
                    ipAddress);
        }
    }

    /**
     * Log other authentication events
     */
    @EventListener
    public void handleAuthenticationEvent(AbstractAuthenticationEvent event) {
        if (!(event instanceof AuthenticationSuccessEvent) && 
            !(event instanceof AuthenticationFailureBadCredentialsEvent) &&
            !(event instanceof AuthenticationFailureDisabledEvent) &&
            !(event instanceof AuthenticationFailureLockedEvent) &&
            !(event instanceof AuthenticationFailureExpiredEvent) &&
            !(event instanceof LogoutSuccessEvent)) {
            
            Authentication auth = event.getAuthentication();
            securityLogger.debug("Authentication event: {}, principal: {}", 
                    event.getClass().getSimpleName(), 
                    auth.getName());
        }
    }
    
    /**
     * Log successful login attempts (called from SecurityConfiguration)
     */
    public void logLoginSuccess(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();
        String ipAddress = extractIpAddress(authentication);
        
        auditLogger.info("LOGIN_SUCCESS user='{}', roles={}, ip={}", username, roles, ipAddress);
        securityLogger.info("User '{}' logged in successfully from IP {}", username, ipAddress);
    }

    /**
     * Log failed login attempts (called from SecurityConfiguration)
     */
    public void logLoginFailure(String username, Exception exception) {
        String reason = "Unknown";
        
        if (exception != null) {
            reason = exception.getClass().getSimpleName();
        }
        
        auditLogger.warn("LOGIN_FAILURE user='{}', reason='{}'", username, reason);
        securityLogger.warn("Failed login attempt for user '{}': {}", username, reason);
    }

    /**
     * Log logout events (called from SecurityConfiguration)
     */
    public void logLogout(Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            String ipAddress = extractIpAddress(authentication);
            
            auditLogger.info("LOGOUT user='{}', ip={}", username, ipAddress);
            securityLogger.info("User '{}' logged out from IP {}", username, ipAddress);
        }
    }

    /**
     * Log access denied events (called from controllers)
     */
    public void logAccessDenied(HttpServletRequest request, String username, String resource) {
        String ipAddress = request.getRemoteAddr();
        
        auditLogger.warn("ACCESS_DENIED user='{}', resource='{}', ip={}", username, resource, ipAddress);
        securityLogger.warn("Access denied for user '{}' to resource '{}' from IP {}", username, resource, ipAddress);
    }

    /**
     * Log sensitive data access (called from services)
     */
    public void logSensitiveDataAccess(String resource, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            String ipAddress = extractIpAddress(auth);
            
            auditLogger.info("SENSITIVE_DATA_ACCESS user='{}', resource='{}', action='{}', ip={}", 
                    username, resource, action, ipAddress);
        }
    }
    
    /**
     * Log administrative actions (called from admin controllers)
     */
    public void logAdminAction(String action, String details) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            String ipAddress = extractIpAddress(auth);
            
            auditLogger.info("ADMIN_ACTION user='{}', action='{}', details='{}', ip={}", 
                    username, action, details, ipAddress);
        }
    }

    /**
     * Extract IP address from authentication details
     */
    private String extractIpAddress(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof WebAuthenticationDetails) {
            return ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
        }
        return "unknown";
    }
    
    /**
     * Extract session ID from authentication details
     */
    private String extractSessionId(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof WebAuthenticationDetails) {
            return ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
        }
        return "unknown";
    }
}
