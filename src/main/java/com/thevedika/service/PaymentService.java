package com.thevedika.service;

import com.thevedika.dto.payment.CreatePaymentOrderResponse;
import com.thevedika.dto.payment.PaymentVerificationResponse;
import com.thevedika.dto.payment.VerifyPaymentRequest;

public interface PaymentService {

    CreatePaymentOrderResponse createOrder(
            String bookingReference,
            String customerPhone);

    PaymentVerificationResponse verifyPayment(
            VerifyPaymentRequest request,
            String customerPhone);
}