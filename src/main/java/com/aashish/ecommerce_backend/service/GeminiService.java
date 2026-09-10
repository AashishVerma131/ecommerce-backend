package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.entity.Product;
import com.aashish.ecommerce_backend.repository.ProductRepository;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final ProductRepository productRepository;

    @Value("${gemini.model}")
    private String model;

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    public String searchProducts(String userQuery) {

        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return "No products are currently available.";
        }

        StringBuilder productCatalog = new StringBuilder();

        for (Product product : products) {

            productCatalog.append("Product ID: ")
                    .append(product.getId())
                    .append("\n");

            productCatalog.append("Name: ")
                    .append(product.getName())
                    .append("\n");

            productCatalog.append("Description: ")
                    .append(product.getDescription())
                    .append("\n");

            productCatalog.append("Price: ")
                    .append(product.getPrice())
                    .append("\n");

            productCatalog.append("Stock: ")
                    .append(product.getStock())
                    .append("\n");

            productCatalog.append("Category: ")
                    .append(product.getCategory())
                    .append("\n");

            productCatalog.append("-------------------------\n");
        }

        String prompt = """
                You are an AI shopping assistant for an e-commerce application.

                The customer has searched for:

                "%s"

                Below is the current product catalog:

                %s

                Your task is to identify the products that best match
                the customer's request.

                Rules:
                1. Recommend only products that exist in the catalog.
                2. Never invent a product.
                3. Never invent a price.
                4. Consider product name, description, category, price,
                   stock and the customer's requirements.
                5. If the customer specifies a maximum price, respect it.
                6. If no product matches, clearly say that no matching
                   product was found.
                7. Keep the response concise and useful.
                8. Include the Product ID, name and price for every
                   recommended product.
                9. Mention briefly why each product matches.
                """.formatted(
                userQuery,
                productCatalog
        );

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        Content content = Content.fromParts(
                Part.fromText(prompt)
        );

        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .temperature(0.2f)
                        .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        content,
                        config
                );

        return response.text();
    }
}