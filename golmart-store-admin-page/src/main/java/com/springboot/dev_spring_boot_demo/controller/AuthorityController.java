package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system")
public class AuthorityController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<String> ALL_ROLES = Arrays.asList("ROLE_ADMIN", "ROLE_SYSTEM");

    public AuthorityController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/authorities")
    public String listAdmins(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        List<Admin> admins = adminService.findAll();
        Map<Admin, List<String>> adminRolesMap = new HashMap<>();
        for (Admin admin : admins) {
            adminRolesMap.put(admin, adminService.getAuthorities(admin.getUsername()));
        }
        model.addAttribute("admins", admins);
        model.addAttribute("adminRolesMap", adminRolesMap);
        return "system/authorities";
    }

    @GetMapping("/authorities/{id}/edit")
    public String showEditAdminForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        Admin admin = adminService.findById(id);
        if (admin == null) {
            return "redirect:/system/authorities";
        }
        List<String> adminRoles = adminService.getAuthorities(admin.getUsername());
        model.addAttribute("admin", admin);
        model.addAttribute("allRoles", ALL_ROLES);
        model.addAttribute("selectedRoles", adminRoles);
        return "system/authority-form";
    }

    @PostMapping("/authorities")
    public String saveAdmin(@ModelAttribute("admin") Admin admin,
                            @RequestParam("roles") List<String> roles) {
        if (admin.getId() != null) {
            Admin existingAdmin = adminService.findById(admin.getId());
            if (existingAdmin == null) {
                // Handle case where admin is not found, e.g., return an error page
                return "redirect:/system/authorities?error=AdminNotFound";
            }
            // Preserve the existing username
            admin.setUsername(existingAdmin.getUsername());
            // Preserve password if not updated
            if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
                admin.setPassword(existingAdmin.getPassword());
            } else {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        } else {
            // New admin, encode the password
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        Admin savedAdmin = adminService.save(admin);

        // Update authorities
        adminService.deleteAuthoritiesByUsername(savedAdmin.getUsername());
        for (String role : roles) {
            Authority authority = new Authority();
            authority.setAdmin(savedAdmin);
            authority.setAuthority(role);
            adminService.saveAuthority(authority);
        }
        return "redirect:/system/authorities";
    }
}