package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.entity.FcmToken;
import com.aashish.ecommerce_backend.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private final FcmTokenRepository fcmTokenRepository;

    // Existing Firebase test notification
    public String sendTestNotification() {

        FcmToken fcmToken = fcmTokenRepository
                .findTopByOrderByUpdatedAtDesc()
                .orElseThrow(() ->
                        new RuntimeException("No FCM token found in database"));

        Notification notification = Notification.builder()
                .setTitle("🎉 Firebase Test")
                .setBody("FCM notification is working successfully!")
                .build();

        Message message = Message.builder()
                .setToken(fcmToken.getToken())
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging
                    .getInstance()
                    .send(message);

            return "FCM notification sent successfully. Message ID: " + response;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to send FCM notification: " + e.getMessage(), e);
        }
    }

    // Real order confirmation notification
    public String sendOrderConfirmation(String orderId, String amount) {

        FcmToken fcmToken = fcmTokenRepository
                .findTopByOrderByUpdatedAtDesc()
                .orElseThrow(() ->
                        new RuntimeException("No FCM token found in database"));

        Notification notification = Notification.builder()
                .setTitle("🎉 Order Confirmed")
                .setBody(
                        "Your order " + orderId +
                                " has been confirmed successfully. Amount: ₹" + amount
                )
                .build();

        Message message = Message.builder()
                .setToken(fcmToken.getToken())
                .setNotification(notification)
                .build();

        try {
            return FirebaseMessaging
                    .getInstance()
                    .send(message);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to send order confirmation notification: "
                            + e.getMessage(), e);
        }
    }
}