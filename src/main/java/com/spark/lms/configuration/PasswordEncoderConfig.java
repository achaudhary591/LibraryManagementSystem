package com.spark.lms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration for password encoder
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Create a BCryptPasswordEncoder bean with strength 12
     * 
     * @return the BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
