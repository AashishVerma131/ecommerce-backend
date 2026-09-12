package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.dto.OrderResponse;
import com.aashish.ecommerce_backend.entity.Order;
import com.aashish.ecommerce_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.placeOrder(
                        authentication.getName()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrders(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getMyOrderById(
            @PathVariable String id,
            Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrderById(
                        id,
                        authentication.getName()
                )
        );
    }
}

