package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    @PostMapping("/send-test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> sendTestMessage(
            @RequestBody Map<String, String> request) {

        String phoneNumber = request.get("phoneNumber");
        String orderId = request.get("orderId");
        String items = request.get("items");
        String amount = request.get("amount");
        String paymentStatus = request.get("paymentStatus");

        String response = whatsAppService.sendOrderConfirmation(
                phoneNumber,
                orderId,
                items,
                amount,
                paymentStatus
        );

        return ResponseEntity.ok(response);
    }
}