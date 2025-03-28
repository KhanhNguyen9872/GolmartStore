package com.springboot.dev_spring_boot_demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartInfo> cartInfos = new ArrayList<>();

    // Default constructor
    public Cart() {}

    // Constructor with user
    public Cart(User user) {
        this.user = user;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartInfo> getCartInfos() {
        return cartInfos;
    }

    public void setCartInfos(List<CartInfo> cartInfos) {
        this.cartInfos = cartInfos;
    }

    // Helper methods for managing cart items
    public void addCartInfo(CartInfo cartInfo) {
        cartInfos.add(cartInfo);
        cartInfo.setCart(this);
    }

    public void removeCartInfo(CartInfo cartInfo) {
        cartInfos.remove(cartInfo);
        cartInfo.setCart(null);
    }
}