package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.User;

public interface UserDAO {
    User findByUsername(String username);
    User save(User user);
}