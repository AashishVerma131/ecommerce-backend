package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.dto.CartItemRequest;
import com.aashish.ecommerce_backend.entity.*;
import com.aashish.ecommerce_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Cart addItem(
            String email,
            CartItemRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository
                .findById(request.productId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"));

        if (product.getStock() < request.quantity()) {
            throw new RuntimeException(
                    "Not enough stock");
        }

        Cart cart = cartRepository
                .findByUserId(user.getId())
                .orElseGet(() ->
                        cartRepository.save(
                                Cart.builder()
                                        .user(user)
                                        .build()
                        )
                );

        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.quantity())
                .build();

        cart.getItems().add(item);

        cartItemRepository.save(item);

        return cart;
    }

    public Cart getCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return cartRepository
                .findByUserId(user.getId())
                .orElseGet(() ->
                        cartRepository.save(
                                Cart.builder()
                                        .user(user)
                                        .build()
                        )
                );
    }

    public void removeItem(String itemId) {

        cartItemRepository.deleteById(itemId);
    }
}