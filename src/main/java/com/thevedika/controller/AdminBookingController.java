package com.thevedika.controller;

import com.thevedika.dto.admin.AdminBookingResponse;
import com.thevedika.entity.Booking;
import com.thevedika.repository.BookingRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.thevedika.dto.admin.AdminBookingActionResponse;
import com.thevedika.service.AdminBookingService;

import java.util.List;

@RestController
@RequestMapping("/admin/bookings")
public class AdminBookingController {

        private final BookingRepository bookingRepository;
        private final AdminBookingService adminBookingService;

        public AdminBookingController(
                        BookingRepository bookingRepository,
                        AdminBookingService adminBookingService) {

                this.bookingRepository = bookingRepository;
                this.adminBookingService = adminBookingService;
        }

        // GET /api/admin/bookings
        @GetMapping
        public List<AdminBookingResponse> getAllBookings() {

                return bookingRepository
                                .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                                .stream()
                                .map(this::convertToResponse)
                                .toList();
        }

        // GET /api/admin/bookings/{id}
        @GetMapping("/{id}")
        public AdminBookingResponse getBookingById(
                        @PathVariable Long id) {

                Booking booking = bookingRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Booking not found"));

                return convertToResponse(booking);
        }

        private AdminBookingResponse convertToResponse(Booking booking) {

                return new AdminBookingResponse(
                                booking.getId(),
                                booking.getBookingReference(),

                                booking.getFarmhouse() != null
                                                ? booking.getFarmhouse().getId()
                                                : null,

                                booking.getFarmhouse() != null
                                                ? booking.getFarmhouse().getName()
                                                : null,

                                booking.getFarmhouse() != null
                                                ? booking.getFarmhouse().getLocation()
                                                : null,

                                booking.getCustomer() != null
                                                ? booking.getCustomer().getId()
                                                : null,

                                booking.getCustomer() != null
                                                ? booking.getCustomer().getFullName()
                                                : null,

                                booking.getCustomer() != null
                                                ? booking.getCustomer().getPhone()
                                                : null,

                                booking.getCheckIn(),
                                booking.getCheckOut(),
                                booking.getGuests(),
                                booking.getTotalNights(),

                                booking.getAccommodationTotal(),
                                booking.getAddonsTotal(),
                                booking.getTotalAmount(),
                                booking.getAmountPaid(),
                                booking.getBalanceDue(),

                                booking.getPaymentOption(),
                                booking.getPaymentStatus(),
                                booking.getBookingStatus(),
                                booking.getSpecialRequests(),

                                booking.getCreatedAt());
        }

        @PutMapping("/{id}/cancel")
        public AdminBookingActionResponse cancelBooking(
                        @PathVariable Long id) {

                return adminBookingService.cancelBooking(id);
        }

        @PutMapping("/{id}/complete")
        public AdminBookingActionResponse completeBooking(
                        @PathVariable Long id) {

                return adminBookingService.completeBooking(id);
        }
}