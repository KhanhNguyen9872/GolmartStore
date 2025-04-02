package com.springboot.dev_spring_boot_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_info")
public class CartInfo {

    @EmbeddedId
    private CartInfoId id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Default constructor
    public CartInfo() {}

    // Parameterized constructor
    public CartInfo(Cart cart, Product product, int quantity) {
        this.id = new CartInfoId(cart.getId(), product.getId());
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and setters
    public CartInfoId getId() {
        return id;
    }

    public void setId(CartInfoId id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}