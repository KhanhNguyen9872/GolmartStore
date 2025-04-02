package com.springboot.dev_spring_boot_demo.dto;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;

    // Constructor
    public CartItemResponse(Long productId, String productName, BigDecimal price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}