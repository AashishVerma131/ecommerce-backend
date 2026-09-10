package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.dto.CartItemRequest;
import com.aashish.ecommerce_backend.entity.Cart;
import com.aashish.ecommerce_backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(
            Authentication authentication,
            @Valid @RequestBody CartItemRequest request) {

        return ResponseEntity.ok(
                cartService.addItem(
                        authentication.getName(),
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(
            Authentication authentication) {

        return ResponseEntity.ok(
                cartService.getCart(
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable String itemId) {

        cartService.removeItem(itemId);

        return ResponseEntity.noContent().build();
    }
}