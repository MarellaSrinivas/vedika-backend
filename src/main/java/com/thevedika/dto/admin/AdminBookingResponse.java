package com.thevedika.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminBookingResponse(
        Long id,
        String bookingReference,

        Long farmhouseId,
        String farmhouseName,
        String location,

        Long customerId,
        String customerName,
        String customerPhone,

        LocalDate checkIn,
        LocalDate checkOut,
        Integer guests,
        Integer totalNights,

        BigDecimal accommodationTotal,
        BigDecimal addonsTotal,
        BigDecimal totalAmount,
        BigDecimal amountPaid,
        BigDecimal balanceDue,

        String paymentOption,
        String paymentStatus,
        String bookingStatus,
        String specialRequests,

        LocalDateTime createdAt) {
}