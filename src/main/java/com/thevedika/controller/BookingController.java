package com.thevedika.controller;

import com.thevedika.dto.booking.BookingResponse;
import com.thevedika.dto.booking.CreateBookingRequest;
import com.thevedika.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/farmhouses/{farmhouseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(
            @PathVariable Long farmhouseId,
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication) {

        return bookingService.createBooking(
                farmhouseId,
                authentication.getName(),
                request);
    }

    @GetMapping("/my-bookings")
    public List<BookingResponse> getMyBookings(
            Authentication authentication) {

        return bookingService.getMyBookings(authentication.getName());
    }

    @GetMapping("/{bookingReference}")
    public BookingResponse getBooking(
            @PathVariable String bookingReference,
            Authentication authentication) {

        return bookingService.getBookingByReference(
                bookingReference,
                authentication.getName());
    }
}