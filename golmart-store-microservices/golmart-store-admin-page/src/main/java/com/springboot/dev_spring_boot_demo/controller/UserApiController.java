package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users - Retrieve all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@AuthenticationPrincipal Admin admin) {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * GET /api/users/{id} - Retrieve a user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * POST /api/users - Create a new user.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user, @AuthenticationPrincipal Admin admin) {
        try {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            User savedUser = userService.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return new ResponseEntity<>("Email already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT /api/users/{id} - Update an existing user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user, @AuthenticationPrincipal Admin admin) {
        try {
            User existingUser = userService.findById(id);
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update fields
            existingUser.setEmail(user.getEmail());
            existingUser.setFullName(user.getFullName());
            // Only update password if provided
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            User updatedUser = userService.save(existingUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return new ResponseEntity<>("Email already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /api/users/{id} - Delete a user by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}