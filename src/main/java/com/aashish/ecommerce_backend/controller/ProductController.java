package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.dto.ProductRequest;
import com.aashish.ecommerce_backend.dto.ProductResponse;
import com.aashish.ecommerce_backend.service.ProductService;
import tools.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductController(
            ProductService productService,
            ObjectMapper objectMapper) {

        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    // =========================================================
    // CREATE PRODUCT - JSON ONLY
    // =========================================================

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ProductResponse createProduct(
            @RequestBody @Valid ProductRequest request) {

        return productService.createProduct(request);
    }


    // =========================================================
    // CREATE PRODUCT - JSON + IMAGE
    // =========================================================

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ProductResponse createProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false)
            MultipartFile image) {

        try {

            ProductRequest request =
                    objectMapper.readValue(
                            productJson,
                            ProductRequest.class
                    );

            return productService.createProduct(
                    request,
                    image
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid product JSON",
                    e
            );
        }
    }


    // =========================================================
    // GET ALL PRODUCTS - ADMIN
    // =========================================================

    @GetMapping
    public java.util.List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }


    // =========================================================
    // GET PRODUCT BY ID - ADMIN
    // =========================================================

    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable String id) {

        return productService.getProductById(id);
    }


    // =========================================================
    // UPDATE PRODUCT - JSON ONLY
    // =========================================================

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ProductResponse updateProduct(
            @PathVariable String id,
            @RequestBody @Valid ProductRequest request) {

        return productService.updateProduct(
                id,
                request
        );
    }


    // =========================================================
    // UPDATE PRODUCT - JSON + IMAGE
    // =========================================================

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ProductResponse updateProductWithImage(
            @PathVariable String id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false)
            MultipartFile image) {

        try {

            ProductRequest request =
                    objectMapper.readValue(
                            productJson,
                            ProductRequest.class
                    );

            return productService.updateProduct(
                    id,
                    request,
                    image
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid product JSON",
                    e
            );
        }
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable String id) {

        productService.deleteProduct(id);
    }
}