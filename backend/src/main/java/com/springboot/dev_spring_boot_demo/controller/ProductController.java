package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Handles GET /admin/products/ to display all products in a table.
     * Uses productService.findAll() (assumed as getAllProducts) and renders admin/products.html.
     */
    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "admin/products";
    }

    /**
     * Handles GET /admin/products/new to show a form for creating a new product.
     */
    @GetMapping("/products/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    /**
     * Handles POST /admin/products to save a new or updated product.
     * Assumes productService.save() exists.
     */
    @PostMapping("/products")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    /**
     * Handles GET /admin/products/{id}/edit to show a form for editing an existing product.
     */
    @GetMapping("/products/{id}/edit")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        return "admin/product-form";
    }

    /**
     * Handles POST /admin/products/{id}/delete to delete a product.
     * Assumes productService.deleteById() exists.
     */
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }
}