package com.aashish.ecommerce_backend.payment;

import com.razorpay.RazorpayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Bean
    public RazorpayClient razorpayClient() throws Exception {

        String keyId = System.getenv("RAZORPAY_KEY_ID");
        String keySecret = System.getenv("RAZORPAY_KEY_SECRET");

        if (keyId == null || keyId.isBlank()) {
            throw new IllegalStateException(
                    "RAZORPAY_KEY_ID environment variable is missing"
            );
        }

        if (keySecret == null || keySecret.isBlank()) {
            throw new IllegalStateException(
                    "RAZORPAY_KEY_SECRET environment variable is missing"
            );
        }

        return new RazorpayClient(keyId, keySecret);
    }
}