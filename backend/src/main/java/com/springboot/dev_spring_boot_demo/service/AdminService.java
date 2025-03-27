package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AdminService extends UserDetailsService {
    List<Admin> findAll();
    Page<Admin> findAll(Pageable pageable);
    Admin findById(Long id);
    Admin save(Admin admin);
    void deleteById(Long id);
    Admin findByUsername(String username);
    List<String> getAuthorities(String username);
    public Admin loadUserByUsername(String username);
}