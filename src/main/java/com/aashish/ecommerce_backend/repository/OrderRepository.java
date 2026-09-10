package com.aashish.ecommerce_backend.repository;

import com.aashish.ecommerce_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository
        extends JpaRepository<Order, String> {

    List<Order> findByUserId(String userId);

    Optional<Order> findByIdAndUserId(String id, String userId);
}