package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AdminDAO {
    List<Admin> findAll();
    Page<Admin> findAll(Pageable pageable);
    Admin findById(Long id);
    Admin save(Admin admin);
    void deleteById(Long id);
    Admin findByUsername(String username);
    List<String> findAuthoritiesByUsername(String username); // New method
}