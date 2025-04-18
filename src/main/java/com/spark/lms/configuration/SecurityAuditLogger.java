package com.spark.lms.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Component to log security-related events
 */
@Component
public class SecurityAuditLogger {

    private static final Logger logger = LoggerFactory.getLogger("org.springframework.security");

    /**
     * Log authentication success events
     * 
     * @param event the authentication success event
     */
    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        logger.info("Login successful for user: {}, authorities: {}, details: {}", 
                auth.getName(), 
                auth.getAuthorities(), 
                auth.getDetails());
    }

    /**
     * Log authentication failure events
     * 
     * @param event the authentication failure event
     */
    @EventListener
    public void authenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        Authentication auth = event.getAuthentication();
        logger.warn("Login failed for user: {}, reason: {}", 
                auth.getName(), 
                event.getException().getMessage());
    }

    /**
     * Log logout success events
     * 
     * @param event the logout success event
     */
    @EventListener
    public void logoutSuccess(LogoutSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        logger.info("Logout successful for user: {}", auth.getName());
    }

    /**
     * Log other authentication events
     * 
     * @param event the authentication event
     */
    @EventListener
    public void handleAuthenticationEvent(AbstractAuthenticationEvent event) {
        if (!(event instanceof AuthenticationSuccessEvent) && 
            !(event instanceof AuthenticationFailureBadCredentialsEvent) &&
            !(event instanceof LogoutSuccessEvent)) {
            
            logger.debug("Authentication event: {}, principal: {}", 
                    event.getClass().getSimpleName(), 
                    event.getAuthentication().getName());
        }
    }
}
