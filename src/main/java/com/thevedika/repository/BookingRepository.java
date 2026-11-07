package com.thevedika.repository;

import com.thevedika.entity.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByBookingReference(String bookingReference);

    List<Booking> findByCustomer_PhoneOrderByCreatedAtDesc(String phone);

    List<Booking> findByFarmhouseIdAndBookingStatusInAndCheckInLessThanAndCheckOutGreaterThan(
            Long farmhouseId,
            List<String> blockingStatuses,
            LocalDate requestedCheckOut,
            LocalDate requestedCheckIn);

    Optional<Booking> findByBookingReference(String bookingReference);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.customer
            WHERE b.bookingReference = :reference
            """)
    Optional<Booking> findByBookingReferenceForUpdate(
            @Param("reference") String reference);
}