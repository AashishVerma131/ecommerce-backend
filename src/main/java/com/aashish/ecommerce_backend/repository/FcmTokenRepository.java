package com.aashish.ecommerce_backend.repository;

import com.aashish.ecommerce_backend.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByToken(String token);
    Optional<FcmToken> findTopByOrderByUpdatedAtDesc();
}