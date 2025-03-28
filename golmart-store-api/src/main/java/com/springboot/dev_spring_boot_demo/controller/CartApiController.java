package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.dto.*;
import com.springboot.dev_spring_boot_demo.entity.Cart;
import com.springboot.dev_spring_boot_demo.entity.CartInfo;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.service.CartService;
import com.springboot.dev_spring_boot_demo.service.JwtUtil;
import com.springboot.dev_spring_boot_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CartApiController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartApiController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    // Get all products in the cart
    @GetMapping("/cart")
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = getCurrentUserId(authHeader);
            Cart cart = cartService.getCartByUserId(userId);

            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Cart not found"));
            }

            List<CartInfo> cartInfos = cartService.getCartInfosByCartId(cart.getId());
            List<CartItemResponse> response = cartInfos.stream()
                    .map(ci -> new CartItemResponse(
                            ci.getProduct().getId(),
                            ci.getProduct().getName(),
                            ci.getProduct().getPrice(),
                            ci.getQuantity()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    // Add a product to the cart (update quantity if exists, create if not)
    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody AddToCartRequest request) {
        try {
            Long userId = getCurrentUserId(authHeader);
            Cart cart = cartService.getCartByUserId(userId);

            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Cart not found"));
            }

            // Delegate to CartService to handle adding or updating quantity
            cartService.addProductToCart(cart.getId(), request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(new SuccessResponse("Product added to cart"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // Remove a product from the cart
    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<?> removeFromCart(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long productId) {
        try {
            Long userId = getCurrentUserId(authHeader);
            Cart cart = cartService.getCartByUserId(userId);

            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Cart not found"));
            }

            cartService.removeProductFromCart(cart.getId(), productId);
            return ResponseEntity.ok(new SuccessResponse("Product removed from cart"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // Change quantity of a product in the cart
    @PutMapping("/cart/{productId}")
    public ResponseEntity<?> changeQuantity(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long productId,
                                            @RequestBody UpdateQuantityRequest request) {
        try {
            Long userId = getCurrentUserId(authHeader);
            Cart cart = cartService.getCartByUserId(userId);

            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Cart not found"));
            }

            if (request.getQuantity() < 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Invalid quantity"));
            }

            cartService.updateProductQuantity(cart.getId(), productId, request.getQuantity());
            return ResponseEntity.ok(new SuccessResponse("Quantity updated"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // Extract user ID from JWT token
    private Long getCurrentUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            String username = JwtUtil.extractUsername(token);
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            return user.getId();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }
}