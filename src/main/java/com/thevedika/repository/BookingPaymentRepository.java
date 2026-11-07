package com.thevedika.repository;

import com.thevedika.entity.BookingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingPaymentRepository
        extends JpaRepository<BookingPayment, Long> {

    Optional<BookingPayment> findByRazorpayOrderId(String orderId);

    Optional<BookingPayment> findByRazorpayPaymentId(String paymentId);
}