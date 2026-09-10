package com.aashish.ecommerce_backend.storage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

@Component
public class BackblazeTest implements CommandLineRunner {

    private final S3Client s3Client;

    public BackblazeTest(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void run(String... args) {

        String bucketName = "ecommerce-product-images-2026";

        s3Client.headBucket(
                HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build()
        );

        System.out.println("=================================");
        System.out.println("BACKBLAZE B2 CONNECTION SUCCESS!");
        System.out.println("Bucket: " + bucketName);
        System.out.println("=================================");
    }
}