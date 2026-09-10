package com.aashish.ecommerce_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequest(

        @NotNull
        String productId,

        @NotNull
        @Min(1)
        Integer quantity
) {
}