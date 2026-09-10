package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.service.FcmNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmNotificationController {

    private final FcmNotificationService fcmNotificationService;

    @PostMapping("/test")
    public ResponseEntity<String> sendTestNotification() {

        return ResponseEntity.ok(
                fcmNotificationService.sendTestNotification()
        );
    }
}