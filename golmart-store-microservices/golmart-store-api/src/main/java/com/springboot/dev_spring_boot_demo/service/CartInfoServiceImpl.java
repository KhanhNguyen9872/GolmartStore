package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.dao.CartDAO;
import com.springboot.dev_spring_boot_demo.dao.CartInfoDAO;
import com.springboot.dev_spring_boot_demo.dao.ProductDAO;
import com.springboot.dev_spring_boot_demo.entity.Cart;
import com.springboot.dev_spring_boot_demo.entity.CartInfo;
import com.springboot.dev_spring_boot_demo.entity.CartInfoId;
import com.springboot.dev_spring_boot_demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartInfoServiceImpl implements CartInfoService {

    @Autowired
    private CartInfoDAO cartInfoDAO;

    @Autowired
    private CartDAO cartDAO; // Assumed DAO for Cart entity

    @Autowired
    private ProductDAO productDAO; // Assumed DAO for Product entity

    @Override
    public List<CartInfo> getCartInfosByCartId(Long cartId) {
        return cartInfoDAO.findByCartId(cartId);
    }

    @Override
    public CartInfo getCartInfoByCartAndProduct(Long cartId, Long productId) {
        return cartInfoDAO.findByCartIdAndProductId(cartId, productId);
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
            // If product exists in cart, increment quantity
            existingCartInfo.setQuantity(existingCartInfo.getQuantity() + quantity);
            cartInfoDAO.save(existingCartInfo);
        } else {
            // Otherwise, create a new cart item
            CartInfo newCartInfo = new CartInfo();
            newCartInfo.setId(new CartInfoId(cartId, productId));
            newCartInfo.setCart(cart);
            newCartInfo.setProduct(product);
            newCartInfo.setQuantity(quantity);
            cartInfoDAO.save(newCartInfo);
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
    public void removeProductFromCart(Long cartId, Long productId) {
        CartInfo cartInfo = cartInfoDAO.findByCartIdAndProductId(cartId, productId);
        if (cartInfo != null) {
            cartInfoDAO.delete(cartInfo);
        }
    }
}