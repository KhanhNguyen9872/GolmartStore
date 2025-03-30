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
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<String> ALL_ROLES = Arrays.asList("ROLE_ADMIN", "ROLE_SYSTEM");

    // Existing dashboard route (preserved)
    @GetMapping
    public String dashboard(Model model, @AuthenticationPrincipal Admin admin) {
        model.addAttribute("fullName", admin.getFullName());
        return "system/index"; // Adjust to your actual admin dashboard template
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
        return "system/admins";
    }

    @GetMapping("/admins/new")
    public String showNewAdminForm(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        model.addAttribute("admin", new Admin());
        return "system/admin-form";
    }

    @GetMapping("/admins/{id}/edit")
    public String showEditAdminForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        Admin admin = adminService.findById(id);
        if (admin == null) {
            return "redirect:/system/admins";
        }
        model.addAttribute("admin", admin);
        return "system/admin-form";
    }

    @PostMapping("/admins")
    public String saveAdmin(@ModelAttribute("admin") Admin admin) {
        if (admin.getId() != null) {
            Admin existingAdmin = adminService.findById(admin.getId());
            if (existingAdmin == null) {
                return "redirect:/system/admins?error=AdminNotFound";
            }
            admin.setUsername(existingAdmin.getUsername());
            if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
                admin.setPassword(existingAdmin.getPassword());
            } else {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        } else {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        adminService.save(admin);
        return "redirect:/system/admins";
    }
}