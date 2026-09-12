package com.aashish.ecommerce_backend.controller;

import com.aashish.ecommerce_backend.dto.FcmTokenRequest;
import com.aashish.ecommerce_backend.entity.FcmToken;
import com.aashish.ecommerce_backend.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    @PostMapping("/token")
    public ResponseEntity<FcmToken> saveToken(
            @RequestBody FcmTokenRequest request) {

        return ResponseEntity.ok(
                fcmTokenService.saveToken(request)
        );
    }
}

