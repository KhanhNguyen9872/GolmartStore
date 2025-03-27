package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.dto.ForgotPasswordRequest;
import com.springboot.dev_spring_boot_demo.dto.LoginRequest;
import com.springboot.dev_spring_boot_demo.dto.UserRegistrationRequest;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserApiController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Login endpoint (handled by Spring Security, but we'll provide a response)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Spring Security handles authentication via /authenticateTheUser
        // This endpoint can return a success message or token if needed
        return ResponseEntity.ok("Login successful - handled by Spring Security");
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationRequest registrationRequest) {
        User existingUser = userService.findByUsername(registrationRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setFullName(registrationRequest.getFullName());
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(true);

        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // Forgot Password endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        User user = userService.findByUsername(forgotPasswordRequest.getUsername());
        if (user == null || !user.getEmail().equals(forgotPasswordRequest.getEmail())) {
            return ResponseEntity.badRequest().body("User not found or email mismatch");
        }

        // Here you would typically generate a reset token and send an email
        // For now, we'll just return a success message
        return ResponseEntity.ok("Password reset initiated - check your email");
    }
}
