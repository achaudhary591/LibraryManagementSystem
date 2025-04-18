package com.spark.lms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Enhanced configuration for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    /**
     * Create an OpenAPI bean with enhanced documentation
     * 
     * @return the OpenAPI configuration
     */
    @Bean
    @Primary
    public OpenAPI enhancedOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LibTrack API")
                        .description("Enhanced API for Library Management System")
                        .version("v1.1")
                        .contact(new Contact()
                                .name("LibTrack Support")
                                .email("support@libtrack.com")
                                .url("https://libtrack.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
