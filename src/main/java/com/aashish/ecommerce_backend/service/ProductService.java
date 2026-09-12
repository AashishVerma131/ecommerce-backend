package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.dto.ProductRequest;
import com.aashish.ecommerce_backend.dto.ProductResponse;
import com.aashish.ecommerce_backend.entity.Product;
import com.aashish.ecommerce_backend.exception.ResourceNotFoundException;
import com.aashish.ecommerce_backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final S3StorageService storageService;

    public ProductService(
            ProductRepository productRepository,
            S3StorageService storageService) {

        this.productRepository = productRepository;
        this.storageService = storageService;
    }

    // =========================================================
    // CREATE PRODUCT - JSON ONLY
    // =========================================================

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .build();

        Product savedProduct =
                productRepository.save(product);

        return mapToResponse(savedProduct);
    }


    // =========================================================
    // CREATE PRODUCT - JSON + IMAGE
    // =========================================================

    @Transactional
    public ProductResponse createProduct(
            ProductRequest request,
            MultipartFile image) {

        String imageUrl = null;

        // Upload image to S3-compatible storage
        if (image != null && !image.isEmpty()) {
            imageUrl = storageService.uploadFile(image);
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .imageUrl(imageUrl)
                .build();

        Product savedProduct =
                productRepository.save(product);

        return mapToResponse(savedProduct);
    }


    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    public ProductResponse getProductById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        return mapToResponse(product);
    }


    // =========================================================
    // UPDATE PRODUCT - JSON ONLY
    // =========================================================

    @Transactional
    public ProductResponse updateProduct(
            String id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());

        // Keep existing image unless a new image URL is supplied
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }

        Product updatedProduct =
                productRepository.save(product);

        return mapToResponse(updatedProduct);
    }


    // =========================================================
    // UPDATE PRODUCT - JSON + IMAGE
    // =========================================================

    @Transactional
    public ProductResponse updateProduct(
            String id,
            ProductRequest request,
            MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());

        // Upload new image only when one is provided
        if (image != null && !image.isEmpty()) {

            String imageUrl =
                    storageService.uploadFile(image);

            product.setImageUrl(imageUrl);
        }

        Product updatedProduct =
                productRepository.save(product);

        return mapToResponse(updatedProduct);
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @Transactional
    public void deleteProduct(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        // Delete image from S3 if product has an image
        if (product.getImageUrl() != null &&
                !product.getImageUrl().isBlank()) {

            String imageUrl = product.getImageUrl();

            String marker = ".amazonaws.com/";

            int index = imageUrl.indexOf(marker);

            if (index != -1) {

                String key =
                        imageUrl.substring(
                                index + marker.length()
                        );

                storageService.deleteFile(key);
            }
        }

        // Delete product from database
        productRepository.delete(product);
    }

    // =========================================================
    // ENTITY → RESPONSE
    // =========================================================

    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
