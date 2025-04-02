package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /api/products - Retrieve all products.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@AuthenticationPrincipal Admin admin) {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * GET /api/products/{id} - Retrieve a product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * POST /api/products - Create a new product with an optional image.
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal Admin admin) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String filename = UUID.randomUUID().toString() + getFileExtension(imageFile.getOriginalFilename());
                Path filePath = Paths.get(uploadDir, filename);
                Files.createDirectories(filePath.getParent());
                Files.copy(imageFile.getInputStream(), filePath);
                product.setImage("/img/" + filename);
            } else if (product.getImage() == null || product.getImage().isEmpty()) {
                return new ResponseEntity<>("Image is required", HttpStatus.BAD_REQUEST);
            }

            Product savedProduct = productService.save(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * PUT /api/products/{id} - Update an existing product with an optional image.
     */
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal Admin admin) {
        try {
            Product existingProduct = productService.findById(id);
            if (existingProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update fields
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());

            // Handle image update if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String filename = UUID.randomUUID().toString() + getFileExtension(imageFile.getOriginalFilename());
                Path filePath = Paths.get(uploadDir, filename);
                Files.createDirectories(filePath.getParent());
                Files.copy(imageFile.getInputStream(), filePath);
                existingProduct.setImage("/img/" + filename);
            }

            Product updatedProduct = productService.save(existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * DELETE /api/products/{id} - Delete a product by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id, @AuthenticationPrincipal Admin admin) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}