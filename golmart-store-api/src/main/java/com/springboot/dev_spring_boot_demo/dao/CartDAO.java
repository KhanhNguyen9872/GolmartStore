package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Cart;

public interface CartDAO {

    Cart findByUserId(Long userId);

    void save(Cart cart);

    void delete(Cart cart);

    Cart findById(Long id);
}