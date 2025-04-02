package com.springboot.dev_spring_boot_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF protection for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/img/**").permitAll()  // Allow all requests to /api/** and /img/**
                        .anyRequest().authenticated()  // All other requests need authentication
                );
        return http.build();
    }
}