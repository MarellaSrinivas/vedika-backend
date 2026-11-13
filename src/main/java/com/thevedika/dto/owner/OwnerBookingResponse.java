package com.thevedika.dto.owner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OwnerBookingResponse(

        Long id,
        String bookingReference,

        String farmhouseName,

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

        LocalDateTime createdAt

) {
}