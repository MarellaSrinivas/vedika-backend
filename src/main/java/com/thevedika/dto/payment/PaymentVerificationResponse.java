package com.thevedika.dto.payment;

import java.math.BigDecimal;

public class PaymentVerificationResponse {

    private boolean success;
    private String bookingReference;
    private String paymentStatus;
    private String bookingStatus;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal balanceDue;
    private String message;

    public PaymentVerificationResponse(
            boolean success,
            String bookingReference,
            String paymentStatus,
            String bookingStatus,
            BigDecimal totalAmount,
            BigDecimal amountPaid,
            BigDecimal balanceDue,
            String message) {

        this.success = success;
        this.bookingReference = bookingReference;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.balanceDue = balanceDue;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public String getMessage() {
        return message;
    }
}