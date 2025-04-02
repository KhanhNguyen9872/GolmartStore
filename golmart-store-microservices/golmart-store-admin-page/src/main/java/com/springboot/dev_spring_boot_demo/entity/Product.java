package com.springboot.dev_spring_boot_demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0", inclusive = true, message = "Price must not lower than 0")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "details")
    private String details;

    @Column(name = "more_about")
    private String moreAbout;

    @Column(name = "image", nullable = false)
    private String image;

    public Product() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMoreAbout() {
        return moreAbout;
    }

    public void setMoreAbout(String moreAbout) {
        this.moreAbout = moreAbout;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", details='" + details + '\'' +
                ", moreAbout='" + moreAbout + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}