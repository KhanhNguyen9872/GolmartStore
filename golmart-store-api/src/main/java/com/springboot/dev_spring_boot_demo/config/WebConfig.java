package com.springboot.dev_spring_boot_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL /img/** to the local directory ./img/
        registry.addResourceHandler("/api/img/**")
                .addResourceLocations("file:./img/");
    }
}
