package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin-panel")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<String> ALL_ROLES = Arrays.asList("ROLE_HOME", "ROLE_ADMIN", "ROLE_SYSTEM");

    // Existing dashboard route (preserved)
    @GetMapping
    public String dashboard(Model model, @AuthenticationPrincipal Admin admin) {
        model.addAttribute("fullName", admin.getFullName());
        return "admin/index"; // Adjust to your actual admin dashboard template
    }

    @GetMapping("/admins")
    public String listAdmins(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        List<Admin> admins = adminService.findAll();
        Map<Admin, List<String>> adminRolesMap = new HashMap<>();
        for (Admin admin : admins) {
            adminRolesMap.put(admin, adminService.getAuthorities(admin.getUsername()));
        }
        model.addAttribute("admins", admins);
        model.addAttribute("adminRolesMap", adminRolesMap);
        return "admin/admins";
    }

    @GetMapping("/admins/new")
    public String showNewAdminForm(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        model.addAttribute("admin", new Admin());
        model.addAttribute("allRoles", ALL_ROLES);
        model.addAttribute("selectedRoles", new ArrayList<String>());
        return "admin/admin-form";
    }

    @GetMapping("/admins/{id}/edit")
    public String showEditAdminForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        Admin admin = adminService.findById(id);
        if (admin == null) {
            return "redirect:/admins";
        }
        List<String> adminRoles = adminService.getAuthorities(admin.getUsername());
        model.addAttribute("admin", admin);
        model.addAttribute("allRoles", ALL_ROLES);
        model.addAttribute("selectedRoles", adminRoles);
        return "admin/admin-form";
    }

    @PostMapping("/admins")
    public String saveAdmin(@ModelAttribute("admin") Admin admin,
                            @RequestParam("roles") List<String> roles) {
        if (admin.getId() != null) {
            Admin existingAdmin = adminService.findById(admin.getId());
            if (existingAdmin == null) {
                // Handle case where admin is not found, e.g., return an error page
                return "redirect:/admin-panel/admins?error=AdminNotFound";
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
        return "redirect:/admin-panel/admins";
    }
}