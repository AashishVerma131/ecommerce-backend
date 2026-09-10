package com.aashish.ecommerce_backend.repository;

import com.aashish.ecommerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository
        extends JpaRepository<CartItem, String> {
}