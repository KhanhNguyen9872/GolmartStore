package com.springboot.dev_spring_boot_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/")
public class LoginController {
    @GetMapping("/admin/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

}
