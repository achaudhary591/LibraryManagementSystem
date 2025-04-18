package com.spark.lms.configuration;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation of AuditorAware to track who created or modified entities
 */
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * Get the current auditor (user)
     * 
     * @return the current username or "system" if no user is authenticated
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("system");
        }
        
        return Optional.of(authentication.getName());
    }
}
