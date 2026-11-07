package com.thevedika.repository;

import com.thevedika.entity.BookingAddon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingAddonRepository
        extends JpaRepository<BookingAddon, Long> {

    List<BookingAddon> findByBookingId(Long bookingId);
}