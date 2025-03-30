package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;
    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String showNewProductForm(Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/products")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model,
                              @AuthenticationPrincipal Admin admin1) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("fullName", admin1.getFullName());
            return "admin/product-form"; // Return to form with errors
        }

        if (!imageFile.isEmpty()) {
            try {
                // Generate a unique filename
                String filename = UUID.randomUUID().toString() + getFileExtension(imageFile.getOriginalFilename());
                Path filePath = Paths.get(uploadDir, filename);

                // Ensure the directory exists
                Files.createDirectories(filePath.getParent());

                // Save the file
                Files.copy(imageFile.getInputStream(), filePath);

                // Set the image URL in the product
                product.setImage("/img/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("fullName", admin1.getFullName());
                model.addAttribute("errorMessage", "Failed to upload image: " + e.getMessage());
                return "admin/product-form"; // Return to form with error
            }
        } else if (product.getImage() == null || product.getImage().isEmpty()) {
            // If no image is uploaded and no existing image is set, manually add an error
            bindingResult.rejectValue("image", "error.image", "Image is required");
            model.addAttribute("fullName", admin1.getFullName());
            return "admin/product-form"; // Return to form with error
        }

        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditProductForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal Admin admin1) {
        model.addAttribute("fullName", admin1.getFullName());
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        return "admin/product-form";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}