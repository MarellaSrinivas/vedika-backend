package com.thevedika.controller;

import com.thevedika.dto.payment.CreatePaymentOrderResponse;
import com.thevedika.dto.payment.PaymentVerificationResponse;
import com.thevedika.dto.payment.VerifyPaymentRequest;
import com.thevedika.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/booking/{bookingReference}/create-order")
    public ResponseEntity<CreatePaymentOrderResponse> createOrder(
            @PathVariable String bookingReference,
            Authentication authentication) {

        return ResponseEntity.ok(
                paymentService.createOrder(
                        bookingReference,
                        authentication.getName()));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerificationResponse> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                paymentService.verifyPayment(
                        request,
                        authentication.getName()));
    }
}