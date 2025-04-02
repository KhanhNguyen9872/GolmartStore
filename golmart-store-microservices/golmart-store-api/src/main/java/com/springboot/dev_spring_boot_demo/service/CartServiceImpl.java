package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.dao.CartDAO;
import com.springboot.dev_spring_boot_demo.dao.CartInfoDAO;
import com.springboot.dev_spring_boot_demo.dao.ProductDAO;
import com.springboot.dev_spring_boot_demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final CartInfoDAO cartInfoDAO;
    private final ProductDAO productDAO;

    @Autowired
    public CartServiceImpl(CartDAO cartDAO, CartInfoDAO cartInfoDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.cartInfoDAO = cartInfoDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartDAO.findByUserId(userId);
    }

    @Override
    public List<CartInfo> getCartInfosByCartId(Long cartId) {
        return cartInfoDAO.findByCartId(cartId);
    }

    @Override
    @Transactional
    public void addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartDAO.findById(cartId);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        CartInfo existingCartInfo = cartInfoDAO.findByCartIdAndProductId(cartId, productId);
        if (existingCartInfo != null) {
            // If product exists, update quantity by adding the new quantity
            existingCartInfo.setQuantity(existingCartInfo.getQuantity() + quantity);
            cartInfoDAO.save(existingCartInfo);
        } else {
            // If product doesn't exist, create a new cart item
            CartInfo newCartInfo = new CartInfo(cart, product, quantity);
            cartInfoDAO.save(newCartInfo);
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        CartInfo cartInfo = cartInfoDAO.findByCartIdAndProductId(cartId, productId);
        if (cartInfo != null) {
            cartInfoDAO.delete(cartInfo);
        }
    }

    @Override
    @Transactional
    public void updateProductQuantity(Long cartId, Long productId, int quantity) {
        CartInfo cartInfo = cartInfoDAO.findByCartIdAndProductId(cartId, productId);
        if (cartInfo == null) {
            throw new RuntimeException("Product not found in cart");
        }
        cartInfo.setQuantity(quantity);
        cartInfoDAO.save(cartInfo);
    }

    @Override
    @Transactional
    public Cart createCartForUser(User user) {
        Cart cart = new Cart(user);
        cartDAO.save(cart);
        return cart;
    }
}