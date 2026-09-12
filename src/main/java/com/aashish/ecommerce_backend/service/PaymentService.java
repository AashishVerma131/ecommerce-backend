package com.aashish.ecommerce_backend.service;

import com.aashish.ecommerce_backend.dto.PaymentOrderResponse;
import com.aashish.ecommerce_backend.dto.PaymentVerificationRequest;
import com.aashish.ecommerce_backend.entity.Order;
import com.aashish.ecommerce_backend.entity.OrderItem;
import com.aashish.ecommerce_backend.entity.Payment;
import com.aashish.ecommerce_backend.entity.PaymentStatus;
import com.aashish.ecommerce_backend.entity.User;
import com.aashish.ecommerce_backend.repository.OrderRepository;
import com.aashish.ecommerce_backend.repository.PaymentRepository;
import com.aashish.ecommerce_backend.repository.UserRepository;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final WhatsAppService whatsAppService;
    private final FcmNotificationService fcmNotificationService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Transactional
    public PaymentOrderResponse createPaymentOrder(
            String orderId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = orderRepository
                .findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found or does not belong to this user"
                        ));

        BigDecimal orderAmount = order.getTotalAmount();

        if (orderAmount == null ||
                orderAmount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Order amount must be greater than zero"
            );
        }

        try {

            RazorpayClient razorpayClient =
                    new RazorpayClient(
                            razorpayKeyId,
                            razorpayKeySecret
                    );

            long amountInPaise =
                    orderAmount
                            .movePointRight(2)
                            .longValueExact();

            JSONObject options = new JSONObject();

            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put(
                    "receipt",
                    "order_" + order.getId()
            );

            OrderClient orderClient =
                    razorpayClient.orders;

            com.razorpay.Order razorpayOrder =
                    orderClient.create(options);

            String razorpayOrderId =
                    razorpayOrder.get("id").toString();

            Payment payment = Payment.builder()
                    .orderId(order.getId())
                    .razorpayOrderId(razorpayOrderId)
                    .amount(orderAmount)
                    .currency("INR")
                    .status(PaymentStatus.CREATED)
                    .createdAt(LocalDateTime.now())
                    .build();

            Payment savedPayment =
                    paymentRepository.save(payment);

            return PaymentOrderResponse.builder()
                    .orderId(order.getId())
                    .razorpayOrderId(
                            savedPayment.getRazorpayOrderId()
                    )
                    .razorpayKeyId(razorpayKeyId)
                    .amount(orderAmount)
                    .currency("INR")
                    .status(
                            savedPayment
                                    .getStatus()
                                    .name()
                    )
                    .build();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create Razorpay order",
                    e
            );
        }
    }

    @Transactional
    public String verifyPayment(
            PaymentVerificationRequest request) {

        try {

            JSONObject verificationData =
                    new JSONObject();

            verificationData.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            verificationData.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            verificationData.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean isValid =
                    Utils.verifyPaymentSignature(
                            verificationData,
                            razorpayKeySecret
                    );

            if (!isValid) {

                throw new RuntimeException(
                        "Invalid Razorpay payment signature"
                );
            }

            Payment payment =
                    paymentRepository
                            .findByRazorpayOrderId(
                                    request.getRazorpayOrderId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Payment record not found"
                                    ));

            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setRazorpaySignature(
                    request.getRazorpaySignature()
            );

            payment.setStatus(
                    PaymentStatus.SUCCESS
            );

            payment.setUpdatedAt(
                    LocalDateTime.now()
            );

            Payment savedPayment =
                    paymentRepository.save(payment);


            // ==========================================
            // GET ORDER
            // ==========================================

            Order order =
                    orderRepository
                            .findById(
                                    savedPayment.getOrderId()
                            )
                            .orElse(null);


            // ==========================================
            // FIREBASE ORDER CONFIRMATION
            // ==========================================

            try {

                if (order != null) {

                    fcmNotificationService
                            .sendOrderConfirmation(
                                    order.getId(),
                                    savedPayment
                                            .getAmount()
                                            .toString()
                            );
                }

            } catch (Exception notificationException) {

                System.err.println(
                        "FCM notification failed: "
                                + notificationException
                                .getMessage()
                );
            }


            // ==========================================
            // WHATSAPP ORDER CONFIRMATION
            // ==========================================

            try {

                if (order != null
                        && order.getUser() != null
                        && order.getUser()
                        .getPhoneNumber() != null
                        && !order.getUser()
                        .getPhoneNumber()
                        .isBlank()) {

                    String phoneNumber =
                            order.getUser()
                                    .getPhoneNumber();


                    // ----------------------------------
                    // BUILD ORDER ITEMS TEXT
                    // ----------------------------------

                    String itemsText =
                            order.getItems()
                                    .stream()
                                    .map(this::formatOrderItem)
                                    .collect(
                                            Collectors.joining("\n")
                                    );


                    // ----------------------------------
                    // ORDER ID
                    // {{1}}
                    // ----------------------------------

                    String orderId =
                            order.getId();


                    // ----------------------------------
                    // TOTAL AMOUNT
                    // {{3}}
                    // ----------------------------------

                    String amount =
                            savedPayment
                                    .getAmount()
                                    .toPlainString();


                    // ----------------------------------
                    // PAYMENT STATUS
                    // {{4}}
                    // ----------------------------------

                    String paymentStatus =
                            "Successful";


                    System.out.println(
                            "========== WHATSAPP ORDER CONFIRMATION =========="
                    );

                    System.out.println(
                            "Phone: " + phoneNumber
                    );

                    System.out.println(
                            "Order ID: " + orderId
                    );

                    System.out.println(
                            "Items: " + itemsText
                    );

                    System.out.println(
                            "Amount: " + amount
                    );

                    System.out.println(
                            "Payment Status: "
                                    + paymentStatus
                    );

                    System.out.println(
                            "================================================="
                    );


                    // ----------------------------------
                    // SEND WHATSAPP TEMPLATE
                    // ----------------------------------

                    whatsAppService
                            .sendOrderConfirmation(
                                    phoneNumber,
                                    orderId,
                                    itemsText,
                                    amount,
                                    paymentStatus
                            );
                }

            } catch (Exception notificationException) {

                System.err.println(
                        "WhatsApp notification failed: "
                                + notificationException
                                .getMessage()
                );

                notificationException.printStackTrace();
            }


            return "Payment verified successfully";

        } catch (Exception e) {

            throw new RuntimeException(
                    "Payment verification failed",
                    e
            );
        }
    }


    // ==============================================
    // FORMAT ONE ORDER ITEM
    // ==============================================

    private String formatOrderItem(
            OrderItem item) {

        String productName =
                item.getProduct()
                        .getName();

        Integer quantity =
                item.getQuantity();

        BigDecimal unitPrice =
                item.getPrice();

        BigDecimal itemTotal =
                unitPrice.multiply(
                        BigDecimal.valueOf(quantity)
                );

        return productName
                + " × "
                + quantity
                + " — ₹"
                + itemTotal.toPlainString();
    }
}
