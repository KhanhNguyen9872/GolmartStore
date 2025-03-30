package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles GET /admin/users to display all users in a table.
     */
    @GetMapping("/users")
    public String listUsers(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    /**
     * Handles GET /admin/users/new to show a form for creating a new user.
     */
    @GetMapping("/users/new")
    public String showNewUserForm(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    /**
     * Handles POST /admin/users to save a new or updated user.
     */
    @PostMapping("/users")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin/users";
    }

    /**
     * Handles GET /admin/users/{id}/edit to show a form for editing an existing user.
     */
    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    /**
     * Handles POST /admin/users/{id}/delete to delete a user.
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}