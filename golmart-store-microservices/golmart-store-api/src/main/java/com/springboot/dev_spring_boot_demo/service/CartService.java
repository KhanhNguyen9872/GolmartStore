package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Cart;
import com.springboot.dev_spring_boot_demo.entity.CartInfo;
import com.springboot.dev_spring_boot_demo.entity.User;
import java.util.List;

public interface CartService {

    Cart getCartByUserId(Long userId);

    List<CartInfo> getCartInfosByCartId(Long cartId);

    void addProductToCart(Long cartId, Long productId, int quantity);

    void removeProductFromCart(Long cartId, Long productId);

    void updateProductQuantity(Long cartId, Long productId, int quantity);

    Cart createCartForUser(User user);
}