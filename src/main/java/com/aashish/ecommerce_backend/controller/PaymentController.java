package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.dto.PaymentOrderResponse;
import com.aashish.ecommerce_backend.dto.PaymentVerificationRequest;
import com.aashish.ecommerce_backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public PaymentOrderResponse createPaymentOrder(
            @PathVariable String orderId,
            Authentication authentication) {

        return paymentService.createPaymentOrder(
                orderId,
                authentication.getName()
        );
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('USER')")
    public String verifyPayment(
            @RequestBody @Valid PaymentVerificationRequest request) {

        return paymentService.verifyPayment(request);
    }
}