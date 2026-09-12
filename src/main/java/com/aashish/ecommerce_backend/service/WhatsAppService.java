package com.aashish.ecommerce_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.access-token}")
    private String accessToken;

    @Value("${whatsapp.api-url}")
    private String apiUrl;

    @Value("${whatsapp.template-name}")
    private String templateName;

    @Value("${whatsapp.template-language}")
    private String templateLanguage;

    /*
     * Public S3 image URL used by the WhatsApp template header.
     */
    @Value("${whatsapp.confirmed-order-image-url}")
    private String confirmedOrderImageUrl;

    private final RestClient restClient =
            RestClient.builder().build();

    public String sendOrderConfirmation(
            String recipientPhoneNumber,
            String orderId,
            String items,
            String amount,
            String paymentStatus) {

        String url =
                apiUrl + "/" + phoneNumberId + "/messages";

        Map<String, Object> body = Map.of(

                "messaging_product",
                "whatsapp",

                "to",
                recipientPhoneNumber,

                "type",
                "template",

                "template",
                Map.of(

                        "name",
                        templateName,

                        "language",
                        Map.of(
                                "code",
                                templateLanguage
                        ),

                        "components",
                        List.of(

                                // IMAGE HEADER
                                Map.of(
                                        "type",
                                        "header",

                                        "parameters",
                                        List.of(
                                                Map.of(
                                                        "type",
                                                        "image",

                                                        "image",
                                                        Map.of(
                                                                "link",
                                                                confirmedOrderImageUrl
                                                        )
                                                )
                                        )
                                ),

                                // BODY VARIABLES
                                Map.of(
                                        "type",
                                        "body",

                                        "parameters",
                                        List.of(

                                                // {{1}} Order ID
                                                Map.of(
                                                        "type",
                                                        "text",

                                                        "text",
                                                        orderId
                                                ),

                                                // {{2}} Items
                                                Map.of(
                                                        "type",
                                                        "text",

                                                        "text",
                                                        items
                                                ),

                                                // {{3}} Total Paid
                                                Map.of(
                                                        "type",
                                                        "text",

                                                        "text",
                                                        amount
                                                ),

                                                // {{4}} Payment Status
                                                Map.of(
                                                        "type",
                                                        "text",

                                                        "text",
                                                        paymentStatus
                                                )
                                        )
                                )
                        )
                )
        );

        System.out.println(
                "========== WHATSAPP REQUEST =========="
        );

        System.out.println("URL: " + url);
        System.out.println(
                "Template: " + templateName
        );
        System.out.println(
                "Language: " + templateLanguage
        );
        System.out.println(
                "Recipient: " + recipientPhoneNumber
        );
        System.out.println(
                "Order ID: " + orderId
        );
        System.out.println(
                "Items: " + items
        );
        System.out.println(
                "Amount: " + amount
        );
        System.out.println(
                "Payment Status: " + paymentStatus
        );
        System.out.println(
                "Image: " + confirmedOrderImageUrl
        );

        System.out.println(
                "======================================"
        );

        try {

            return restClient.post()
                    .uri(url)
                    .contentType(
                            MediaType.APPLICATION_JSON
                    )
                    .header(
                            "Authorization",
                            "Bearer " + accessToken
                    )
                    .body(body)
                    .retrieve()
                    .body(String.class);

        } catch (Exception e) {

            System.err.println(
                    "========== WHATSAPP ERROR =========="
            );

            System.err.println(
                    "Error message: "
                            + e.getMessage()
            );

            e.printStackTrace();

            System.err.println(
                    "===================================="
            );

            throw e;
        }
    }
}
