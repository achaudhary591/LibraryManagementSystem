package com.spark.lms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for JPA auditing
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfiguration {

    /**
     * Create an AuditorAware bean
     * 
     * @return the AuditorAware implementation
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditAwareImpl();
    }
}
