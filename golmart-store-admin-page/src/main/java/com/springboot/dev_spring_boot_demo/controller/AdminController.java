package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Existing dashboard route (preserved)
    @GetMapping
    public String dashboard(Model model, @AuthenticationPrincipal Admin admin) {
        model.addAttribute("fullName", admin.getFullName());
        return "admin/index"; // Adjust to your actual admin dashboard template
    }

    @GetMapping("/redirect")
    public String checkRole(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Get the roles of the authenticated user
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();

                // Check the role and redirect accordingly
                if ("ROLE_SYSTEM".equals(role)) {
                    return "redirect:/system";
                } else if ("ROLE_ADMIN".equals(role)) {
                    return "redirect:/admin";  // Redirect to /admin
                }
            }
        }

        // If the role is neither SYSTEM nor ADMIN, show 403 restricted page
        return "error/403";
    }
}