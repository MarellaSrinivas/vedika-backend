
package com.thevedika.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminPaymentResponse(
        Long id,
        Long bookingId,
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
        LocalDateTime createdAt) {
}
