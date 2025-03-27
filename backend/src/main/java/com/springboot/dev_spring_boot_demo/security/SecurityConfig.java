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
    private final UserDetailsService userUserDetailsService;

    @Autowired
    public SecurityConfig(AdminService adminService, UserDetailsService userUserDetailsService) {
        this.adminService = adminService;
        this.userUserDetailsService = userUserDetailsService;
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
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/login", "/logout").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/admin", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/accessDenied")
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