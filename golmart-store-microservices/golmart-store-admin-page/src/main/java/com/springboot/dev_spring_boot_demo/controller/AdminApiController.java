package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class AdminApiController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * GET /api/admins - Retrieve all admins with their roles.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAdmins(@AuthenticationPrincipal Admin admin) {
        List<Admin> admins = adminService.findAll();
        Map<Admin, List<String>> adminRolesMap = new HashMap<>();
        for (Admin a : admins) {
            adminRolesMap.put(a, adminService.getAuthorities(a.getUsername()));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("admins", admins);
        response.put("adminRolesMap", adminRolesMap);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET /api/admins/{id} - Retrieve an admin by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        Admin foundAdmin = adminService.findById(id);
        if (foundAdmin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundAdmin, HttpStatus.OK);
    }

    /**
     * POST /api/admins - Create a new admin.
     */
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin, @AuthenticationPrincipal Admin currentAdmin) {
        try {
            // Check for duplicate username
            Admin existingAdmin = adminService.findByUsername(admin.getUsername());
            if (existingAdmin != null) {
                return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
            }

            // Encode password
            if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            } else {
                return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
            }

            Admin savedAdmin = adminService.save(admin);
            return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT /api/admins/{id} - Update an existing admin.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable("id") Long id, @RequestBody Admin admin, @AuthenticationPrincipal Admin currentAdmin) {
        try {
            Admin existingAdmin = adminService.findById(id);
            if (existingAdmin == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Prevent changing username to an existing one
            if (!existingAdmin.getUsername().equals(admin.getUsername())) {
                Admin duplicateAdmin = adminService.findByUsername(admin.getUsername());
                if (duplicateAdmin != null) {
                    return new ResponseEntity<>("Username is already taken", HttpStatus.CONFLICT);
                }
            }

            // Update fields
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setFullName(admin.getFullName());
            // Only update password if provided
            if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
                existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }

            Admin updatedAdmin = adminService.save(existingAdmin);
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /api/admins/{id} - Delete an admin by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        Admin foundAdmin = adminService.findById(id);
        if (foundAdmin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adminService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}