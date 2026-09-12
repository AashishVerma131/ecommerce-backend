package com.aashish.ecommerce_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // No more "throws IOException" here — callers (ProductService) should not
    // need to know or care that this implementation happens to use a stream
    // internally. The exception is caught and converted at the source.
    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();

        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf(".")
            );
        }

        String fileName = UUID.randomUUID() + extension;

        String key = "products/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            // file.getBytes() is the only checked-exception source here.
            // Wrap it as unchecked so ProductService doesn't need a try/catch
            // or a "throws IOException" on every method that uploads an image.
            throw new RuntimeException("Failed to read image file bytes for upload", e);
        }

        return "https://" + bucketName
                + ".s3."
                + region
                + ".amazonaws.com/"
                + key;
    }

    public void deleteFile(String key) {

        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}