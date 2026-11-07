package com.thevedika.service;

import com.thevedika.dto.booking.BookingResponse;
import com.thevedika.dto.booking.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(
            Long farmhouseId,
            String customerPhone,
            CreateBookingRequest request);

    List<BookingResponse> getMyBookings(String customerPhone);

    BookingResponse getBookingByReference(
            String bookingReference,
            String customerPhone);
}