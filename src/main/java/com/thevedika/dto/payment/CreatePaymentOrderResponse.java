package com.thevedika.dto.payment;

public class CreatePaymentOrderResponse {

    private String keyId;
    private String orderId;
    private Long amount;
    private String currency;
    private String bookingReference;
    private String paymentOption;
    private String customerPhone;

    public CreatePaymentOrderResponse(
            String keyId,
            String orderId,
            Long amount,
            String currency,
            String bookingReference,
            String paymentOption,
            String customerPhone) {

        this.keyId = keyId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.bookingReference = bookingReference;
        this.paymentOption = paymentOption;
        this.customerPhone = customerPhone;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
}