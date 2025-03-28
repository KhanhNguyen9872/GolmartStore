package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.CartInfo;

import java.util.List;

public interface CartInfoService {

    List<CartInfo> getCartInfosByCartId(Long cartId);

    CartInfo getCartInfoByCartAndProduct(Long cartId, Long productId);

    void addProductToCart(Long cartId, Long productId, int quantity);

    void updateProductQuantity(Long cartId, Long productId, int quantity);

    void removeProductFromCart(Long cartId, Long productId);
}