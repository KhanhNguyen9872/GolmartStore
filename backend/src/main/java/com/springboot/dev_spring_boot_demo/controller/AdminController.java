package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String dashboard(Model model, @AuthenticationPrincipal Admin admin) {
        model.addAttribute("fullName", admin.getFullName());
        return "admin/index"; // Adjust to your actual admin dashboard template
    }
}