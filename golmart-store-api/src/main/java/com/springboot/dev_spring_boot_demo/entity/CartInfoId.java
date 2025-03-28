package com.springboot.dev_spring_boot_demo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartInfoId implements Serializable {

    private Long cartId;
    private Long productId;

    public CartInfoId() {}

    public CartInfoId(Long cartId, Long productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    // Getters, setters, equals, and hashCode
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartInfoId that = (CartInfoId) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId);
    }
}