package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityApiController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<String> ALL_ROLES = Arrays.asList("ROLE_ADMIN", "ROLE_SYSTEM");

    @Autowired
    public AuthorityApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /api/authorities - Retrieve all admins with their roles.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAdminsWithRoles(@AuthenticationPrincipal Admin admin) {
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
     * GET /api/authorities/{id} - Retrieve an admin's roles by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAdminRoles(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        Admin foundAdmin = adminService.findById(id);
        if (foundAdmin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> adminRoles = adminService.getAuthorities(foundAdmin.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("admin", foundAdmin);
        response.put("roles", adminRoles);
        response.put("allRoles", ALL_ROLES);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * PUT /api/authorities/{id} - Update an admin's roles.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdminRoles(
            @PathVariable("id") Long id,
            @RequestBody Map<String, List<String>> requestBody,
            @AuthenticationPrincipal Admin currentAdmin) {
        try {
            Admin existingAdmin = adminService.findById(id);
            if (existingAdmin == null) {
                return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);
            }

            List<String> roles = requestBody.get("roles");
            if (roles == null || roles.isEmpty()) {
                return new ResponseEntity<>("At least one role must be selected", HttpStatus.BAD_REQUEST);
            }

            // Validate roles against ALL_ROLES
            for (String role : roles) {
                if (!ALL_ROLES.contains(role)) {
                    return new ResponseEntity<>("Invalid role: " + role, HttpStatus.BAD_REQUEST);
                }
            }

            // Update authorities
            adminService.deleteAuthoritiesByUsername(existingAdmin.getUsername());
            for (String role : roles) {
                Authority authority = new Authority();
                authority.setAdmin(existingAdmin);
                authority.setAuthority(role);
                adminService.saveAuthority(authority);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}