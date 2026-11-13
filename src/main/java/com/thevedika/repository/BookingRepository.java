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

        // Customer booking history
        List<Booking> findByCustomer_PhoneOrderByCreatedAtDesc(
                        String phone);

        // Admin: all bookings
        List<Booking> findAllByOrderByCreatedAtDesc();

        // Owner: bookings for their farmhouses
        List<Booking> findByFarmhouse_Owner_IdOrderByCreatedAtDesc(
                        Long ownerId);

        // Dashboard booking counts
        long countByBookingStatus(String bookingStatus);

        long countByFarmhouse_Owner_IdAndBookingStatus(
                        Long ownerId,
                        String bookingStatus);

        long countByFarmhouse_Owner_Id(Long ownerId);

        // Existing availability overlap check
        List<Booking> findByFarmhouseIdAndBookingStatusInAndCheckInLessThanAndCheckOutGreaterThan(
                        Long farmhouseId,
                        List<String> blockingStatuses,
                        LocalDate requestedCheckOut,
                        LocalDate requestedCheckIn);

        Optional<Booking> findByBookingReference(String bookingReference);

        // Existing payment verification lock query
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT b FROM Booking b
                        JOIN FETCH b.customer
                        WHERE b.bookingReference = :reference
                        """)
        Optional<Booking> findByBookingReferenceForUpdate(
                        @Param("reference") String reference);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT b FROM Booking b WHERE b.id = :id")
        Optional<Booking> findByIdForUpdate(@Param("id") Long id);

}