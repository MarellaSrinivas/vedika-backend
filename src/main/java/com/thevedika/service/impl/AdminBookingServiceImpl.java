
package com.thevedika.service.impl;

import com.thevedika.dto.admin.AdminBookingActionResponse;
import com.thevedika.entity.Booking;
import com.thevedika.repository.BookingRepository;
import com.thevedika.service.AdminBookingService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AdminBookingServiceImpl implements AdminBookingService {

    private final BookingRepository bookingRepository;

    public AdminBookingServiceImpl(
            BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public AdminBookingActionResponse cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"));

        if (!"PENDING_PAYMENT".equalsIgnoreCase(
                booking.getBookingStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending-payment bookings can be cancelled here. "
                            + "Paid or confirmed bookings require the refund workflow.");
        }

        BigDecimal amountPaid = booking.getAmountPaid() == null
                ? BigDecimal.ZERO
                : booking.getAmountPaid();

        if (amountPaid.compareTo(BigDecimal.ZERO) > 0
                || !"UNPAID".equalsIgnoreCase(
                        booking.getPaymentStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This booking has a payment record. "
                            + "Use the refund workflow before cancelling it.");
        }

        booking.setBookingStatus("CANCELLED");

        Booking saved = bookingRepository.save(booking);

        return new AdminBookingActionResponse(
                saved.getId(),
                saved.getBookingReference(),
                saved.getBookingStatus(),
                saved.getPaymentStatus(),
                "Booking cancelled successfully");
    }

    @Override
    @Transactional
    public AdminBookingActionResponse completeBooking(Long bookingId) {

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"));

        if (!"CONFIRMED".equalsIgnoreCase(
                booking.getBookingStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only confirmed bookings can be completed");
        }

        if (booking.getCheckOut() == null
                || booking.getCheckOut().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A booking can only be completed on or after its checkout date");
        }

        booking.setBookingStatus("COMPLETED");

        Booking saved = bookingRepository.save(booking);

        return new AdminBookingActionResponse(
                saved.getId(),
                saved.getBookingReference(),
                saved.getBookingStatus(),
                saved.getPaymentStatus(),
                "Booking marked as completed");
    }
}
