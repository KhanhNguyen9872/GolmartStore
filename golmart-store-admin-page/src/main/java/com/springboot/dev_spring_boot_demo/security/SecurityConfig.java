package com.springboot.dev_spring_boot_demo.security;

import com.springboot.dev_spring_boot_demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminService adminService; // Updated to use AdminService directly

    @Autowired
    public SecurityConfig(AdminService adminService) {
        this.adminService = adminService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/admin-panel/admins/**").hasRole("SYSTEM")    // Restrict /admin-panel/admins and subpaths to ROLE_SYSTEM
                                .requestMatchers("/admin-panel/users/**").hasRole("ADMIN")     // Restrict /admin-panel/users and subpaths to ROLE_ADMIN
                                .requestMatchers("/admin-panel/**").authenticated()            // Allow all authenticated users to /admin-panel and subpaths
                                .requestMatchers("/admin-panel/login", "/admin-panel/logout").permitAll()              // Allow public access to login and logout
                                .anyRequest().authenticated()                                  // All other requests require authentication
                )
                .formLogin(form ->
                        form
                                .loginPage("/admin-panel/login")
                                .loginProcessingUrl("/admin-panel/authenticateTheUser")
                                .defaultSuccessUrl("/admin-panel", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/admin-panel/logout").logoutRequestMatcher(new AntPathRequestMatcher("/admin-panel/logout", "GET"))
                                .logoutSuccessUrl("/admin-panel/login?logout")
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/admin-panel/accessDenied")
                )
                .userDetailsService(username -> {
                    try {
                        return adminService.loadUserByUsername(username);
                    } catch (UsernameNotFoundException e) {
                        return null;
                    }
                });

        return http.build();
    }
}