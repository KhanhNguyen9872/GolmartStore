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
                                // Allow public access to static resources
                                .requestMatchers(
                                        "/admin/img/**",
                                        "/admin/css/**",
                                        "/admin/images/**",
                                        "/admin/js/**",
                                        "/admin/scss/**",
                                        "/admin/vendor/**",
                                        "/admin/fonts/**",
                                        "/admin/static/**"  // Add any other static paths as needed
                                ).permitAll()
                                .requestMatchers(
                                        "/admin/redirect"
                                ).authenticated()
                                .requestMatchers(
                                        "/admin/accessDenied"
                                ).permitAll()
                                // Role-based access for specific paths
                                .requestMatchers("/system/**", "/api/admins/**", "/api/authorities/**").hasRole("SYSTEM")
                                .requestMatchers("/admin/**", "/api/users/**", "/api/products/**").hasRole("ADMIN")
                                .requestMatchers("/admin/login", "/admin/logout").permitAll()
                                .anyRequest().authenticated()  // All other requests require authentication
                )
                .formLogin(form ->
                        form
                                .loginPage("/admin/login")
                                .loginProcessingUrl("/admin/authenticateTheUser")
                                .defaultSuccessUrl("/admin/redirect", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/admin/logout").logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout", "GET"))
                                .logoutSuccessUrl("/admin/login?logout")
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/admin/accessDenied")  // Access denied (403)
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