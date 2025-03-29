package com.springboot.dev_spring_boot_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Map URL /profile_pictures/** to the local directory ./profile_pictures/
        registry.addResourceHandler("/profile_pictures/**")
                .addResourceLocations("file:./profile_pictures/");

        registry.addResourceHandler("/admin-panel/img/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
