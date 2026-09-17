package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.entity.FcmToken;
import com.aashish.ecommerce_backend.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private final FcmTokenRepository fcmTokenRepository;

    // Existing Firebase test notification
    public String sendTestNotification() {

        FcmToken fcmToken = fcmTokenRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "No FCM token found in database"));

        Notification notification = Notification.builder()
                .setTitle("🎉 Firebase Test")
                .setBody(
                        "FCM notification is working successfully!"
                )
                .build();

        Message message = Message.builder()
                .setToken(fcmToken.getToken())
                .setNotification(notification)
                .build();

        try {

            String response =
                    FirebaseMessaging
                            .getInstance()
                            .send(message);

            return "FCM notification sent successfully. Message ID: "
                    + response;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send FCM notification: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // Real order confirmation notification
    public String sendOrderConfirmation(
            String userId,
            String orderId,
            String amount) {

        List<FcmToken> fcmTokens =
                fcmTokenRepository.findByUserId(userId);

        if (fcmTokens.isEmpty()) {
            throw new RuntimeException(
                    "No FCM token found for user: " + userId
            );
        }

        Notification notification =
                Notification.builder()
                        .setTitle("🎉 Order Confirmed")
                        .setBody(
                                "Your order "
                                        + orderId
                                        + " has been confirmed successfully. Amount: ₹"
                                        + amount
                        )
                        .build();

        for (FcmToken fcmToken : fcmTokens) {

            Message message = Message.builder()
                    .setToken(fcmToken.getToken())
                    .setNotification(notification)
                    .build();

            try {

                FirebaseMessaging
                        .getInstance()
                        .send(message);

            } catch (Exception e) {

                System.err.println(
                        "Failed to send FCM notification to token: "
                                + fcmToken.getToken()
                                + " - "
                                + e.getMessage()
                );
            }
        }

        return "Order confirmation notification sent successfully";
        }
}

