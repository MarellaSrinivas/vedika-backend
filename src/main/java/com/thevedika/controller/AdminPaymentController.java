
package com.thevedika.controller;

import com.thevedika.dto.admin.AdminPaymentResponse;
import com.thevedika.entity.Booking;
import com.thevedika.entity.BookingPayment;
import com.thevedika.repository.BookingPaymentRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/payments")
@Transactional(readOnly = true)
public class AdminPaymentController {

    private final BookingPaymentRepository paymentRepository;

    public AdminPaymentController(
            BookingPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping
    public List<AdminPaymentResponse> getAllPayments() {

        return paymentRepository
                .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AdminPaymentResponse getPaymentById(
            @PathVariable Long id) {

        BookingPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Payment not found"));

        return convertToResponse(payment);
    }

    private AdminPaymentResponse convertToResponse(
            BookingPayment payment) {

        Booking booking = payment.getBooking();

        return new AdminPaymentResponse(
                payment.getId(),

                booking != null ? booking.getId() : null,
                booking != null ? booking.getBookingReference() : null,
                booking != null && booking.getFarmhouse() != null
                        ? booking.getFarmhouse().getName()
                        : null,

                booking != null && booking.getCustomer() != null
                        ? booking.getCustomer().getFullName()
                        : null,
                booking != null && booking.getCustomer() != null
                        ? booking.getCustomer().getPhone()
                        : null,

                payment.getRazorpayOrderId(),
                payment.getRazorpayPaymentId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentOption(),
                payment.getStatus(),
                payment.getCreatedAt());
    }
}
