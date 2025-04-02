package com.springboot.dev_spring_boot_demo.dto;

public class UpdateQuantityRequest {
    private int quantity;

    // Default constructor
    public UpdateQuantityRequest() {}

    // Getters and setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}