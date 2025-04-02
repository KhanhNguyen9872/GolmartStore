package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    // Get single product by ID
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    // Search products by name containing query text (case-insensitive)
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(value = "q", required = false) String query) {
        List<Product> products;
        if (query == null || query.trim().isEmpty()) {
            // If query is null or empty (including only whitespace), return all products
            products = productService.findAll();
        } else {
            // Convert query to lowercase and trim whitespace
            String lowerQuery = query.toLowerCase().trim();
            // Filter products where name contains the query text (case-insensitive)
            products = productService.findAll().stream()
                    .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(lowerQuery))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }
}