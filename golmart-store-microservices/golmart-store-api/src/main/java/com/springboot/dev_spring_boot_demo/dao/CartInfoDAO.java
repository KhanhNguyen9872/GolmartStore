package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.CartInfo;

import java.util.List;

public interface CartInfoDAO {

    List<CartInfo> findByCartId(Long cartId);

    CartInfo findByCartIdAndProductId(Long cartId, Long productId);

    void save(CartInfo cartInfo);

    void delete(CartInfo cartInfo);
}