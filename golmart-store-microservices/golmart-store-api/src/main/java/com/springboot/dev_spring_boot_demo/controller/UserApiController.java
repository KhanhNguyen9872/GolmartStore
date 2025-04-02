package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.dto.*;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.service.CartService;
import com.springboot.dev_spring_boot_demo.service.JwtUtil;
import com.springboot.dev_spring_boot_demo.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Autowired
    public UserApiController(UserService userService, PasswordEncoder passwordEncoder, CartService cartService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    // Login endpoint: Returns a JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
        }
        String token = JwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // Get user info: Verifies JWT and returns user details
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header"));
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            String username = JwtUtil.extractUsername(token);
            User user = userService.findByUsername(username);
            if (user != null) {
                return ResponseEntity.ok(new UserInfoResponse(user.getUsername(), user.getFullName(), user.getEmail()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Token has expired"));
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Malformed token"));
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid token signature"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid token"));
        }
    }

    // Register endpoint: Creates a new user and a cart
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        if (userService.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Username already exists"));
        }
        if (userService.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Email already exists"));
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setEnabled(true);

        // Save the user
        User savedUser = userService.save(user);

        // Create a cart for the user
        cartService.createCartForUser(savedUser);

        return ResponseEntity.ok(new UserInfoResponse(
                savedUser.getUsername(),
                savedUser.getFullName(),
                savedUser.getEmail()
        ));
    }

    // Forgot password endpoint: Generates new password and returns it in the response
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null || !user.getEmail().equals(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found or email mismatch"));
        }

        // Generate a random password
        String newPassword = generateRandomPassword(12); // 12 characters long
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Update user's password in the database
        user.setPassword(encodedPassword);
        userService.save(user);

        // Return the new password in the response
        return ResponseEntity.ok(new SuccessResponse("Your new password is: " + newPassword));
    }

    // Helper method to generate a random password
    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }
}