package com.thevedika.dto.owner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OwnerPaymentResponse(

        Long id,

        String bookingReference,
        String farmhouseName,

        String customerName,
        String customerPhone,

        String razorpayOrderId,
        String razorpayPaymentId,

        BigDecimal amount,
        String currency,

        String paymentOption,
        String status,

        LocalDateTime createdAt

) {
}