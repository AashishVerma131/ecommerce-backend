package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.dto.LoginRequest;
import com.aashish.ecommerce_backend.dto.LoginResponse;
import com.aashish.ecommerce_backend.dto.RegisterRequest;
import com.aashish.ecommerce_backend.entity.User;
import com.aashish.ecommerce_backend.repository.UserRepository;
import com.aashish.ecommerce_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(request.role())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}