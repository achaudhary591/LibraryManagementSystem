package com.spark.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configure OpenAPI with security schemes
     */
    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("basicAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("basicAuth", 
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("Basic authentication")))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("LibTrack API")
                        .version("1.0")
                        .description("API documentation for LibTrack Library Management System")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("LibTrack Support")
                                .email("support@libtrack.com"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
