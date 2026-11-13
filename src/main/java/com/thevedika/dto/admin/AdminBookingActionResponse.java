
package com.thevedika.dto.admin;

public record AdminBookingActionResponse(
        Long id,
        String bookingReference,
        String bookingStatus,
        String paymentStatus,
        String message) {
}
