
package com.thevedika.service;

import com.thevedika.dto.admin.AdminBookingActionResponse;

public interface AdminBookingService {

    AdminBookingActionResponse cancelBooking(Long bookingId);

    AdminBookingActionResponse completeBooking(Long bookingId);
}
