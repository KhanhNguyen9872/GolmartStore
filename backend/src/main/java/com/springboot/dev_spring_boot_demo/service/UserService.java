package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.User;

public interface UserService {
    User findByUsername(String username);
    User save(User user);
}