package com.spark.lms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration for the application
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // Add specific handler for favicon.ico
        registry
                .addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico");
    }

    /**
     * Configure CORS for the application
     * Restricts cross-origin requests to specific origins, methods, and headers
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/rest/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                .exposedHeaders("Custom-Header")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Add interceptors to the registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add logging interceptor for all requests
        registry.addInterceptor(loggingInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**");
        
        // Add performance monitoring interceptor
        registry.addInterceptor(performanceInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    /**
     * Create a logging interceptor bean
     */
    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
    
    /**
     * Create a performance monitoring interceptor bean
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}
