package com.aashish.ecommerce_backend.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class BackblazeStorageService {

    private static final String BUCKET_NAME =
            "ecommerce-product-images-2026";

    private static final String ENDPOINT =
            "https://s3.us-east-005.backblazeb2.com";

    private final S3Client s3Client;

    public BackblazeStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String objectKey =
                "products/" + UUID.randomUUID() + extension;

        try {

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectKey)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return ENDPOINT + "/" + BUCKET_NAME + "/" + objectKey;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to upload file",
                    e
            );
        }
    }

    public void deleteFile(String objectKey) {

        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(objectKey)
                        .build();

        s3Client.deleteObject(request);
    }
}