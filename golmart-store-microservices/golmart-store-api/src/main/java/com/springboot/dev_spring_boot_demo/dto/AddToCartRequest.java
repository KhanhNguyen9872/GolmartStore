package com.springboot.dev_spring_boot_demo.dto;

public class AddToCartRequest {
    private Long productId;
    private int quantity;

    // Default constructor
    public AddToCartRequest() {}

    // Getters and setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}