package com.spark.lms.confguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@PropertySource({"classpath:/properties/lms.properties", "classpath:/properties/messages.properties"})
public class WebMvcConfig implements WebMvcConfigurer {
    // Bean removed to avoid duplication with WebConfig
}
