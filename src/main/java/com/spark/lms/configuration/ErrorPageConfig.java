package com.spark.lms.configuration;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Configuration for custom error pages
 */
@Configuration
public class ErrorPageConfig {

    /**
     * Register custom error pages for different HTTP status codes
     */
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageRegistrar() {
            @Override
            public void registerErrorPages(ErrorPageRegistry registry) {
                // Register custom error pages
                registry.addErrorPages(
                    new ErrorPage(HttpStatus.NOT_FOUND, "/error/not-found"),
                    new ErrorPage(HttpStatus.FORBIDDEN, "/error/access-denied"),
                    new ErrorPage(HttpStatus.BAD_REQUEST, "/error/bad-request"),
                    new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/general-error"),
                    new ErrorPage(Throwable.class, "/error/general-error")
                );
            }
        };
    }
}
