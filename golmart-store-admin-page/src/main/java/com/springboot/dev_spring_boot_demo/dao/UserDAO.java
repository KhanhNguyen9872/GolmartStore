package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.User;
import java.util.List;

public interface UserDAO {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void deleteById(Long id);
}