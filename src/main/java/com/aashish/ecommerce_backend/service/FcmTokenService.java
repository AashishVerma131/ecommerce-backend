package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.dto.FcmTokenRequest;
import com.aashish.ecommerce_backend.entity.FcmToken;
import com.aashish.ecommerce_backend.entity.User;
import com.aashish.ecommerce_backend.repository.FcmTokenRepository;
import com.aashish.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    public FcmToken saveToken(
            FcmTokenRequest request,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        FcmToken token = fcmTokenRepository
                .findByToken(request.getToken())
                .orElseGet(FcmToken::new);

        token.setToken(request.getToken());
        token.setUser(user);

        if (token.getCreatedAt() == null) {
            token.setCreatedAt(LocalDateTime.now());
        }

        token.setUpdatedAt(LocalDateTime.now());

        return fcmTokenRepository.save(token);
    }
}