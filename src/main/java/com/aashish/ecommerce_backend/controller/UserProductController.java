package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.dto.ProductResponse;
import com.aashish.ecommerce_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class UserProductController {

    private final ProductService productService;

    // USER + ADMIN can view all products
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    // USER + ADMIN can view one product
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProductResponse getProductById(
            @PathVariable String id) {

        return productService.getProductById(id);
    }
}