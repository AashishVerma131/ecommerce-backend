package com.aashish.ecommerce_backend.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class BackblazeConfig {

    @Bean
    public S3Client s3Client() {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                System.getenv("BACKBLAZE_ACCESS_KEY"),
                System.getenv("BACKBLAZE_SECRET_KEY")
        );

        return S3Client.builder()
                .endpointOverride(
                        URI.create("https://s3.us-east-005.backblazeb2.com")
                )
                .region(Region.of("us-east-005"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }
}