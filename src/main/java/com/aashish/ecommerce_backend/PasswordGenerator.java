package com.aashish.ecommerce_backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "Test@12345";

        System.out.println(
                encoder.encode(password)
        );
    }
}