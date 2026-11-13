package com.thevedika.repository;

import com.thevedika.entity.BookingPayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookingPaymentRepository
                extends JpaRepository<BookingPayment, Long> {

        Optional<BookingPayment> findByRazorpayOrderId(String orderId);

        Optional<BookingPayment> findByRazorpayPaymentId(String paymentId);

        // Admin: all payment records
        List<BookingPayment> findAllByOrderByCreatedAtDesc();

        // Owner: payments related to their farmhouses
        List<BookingPayment> findByBooking_Farmhouse_Owner_IdOrderByCreatedAtDesc(
                        Long ownerId);

        // Payment totals for admin dashboard
        @Query("""
                        SELECT COALESCE(SUM(p.amount), 0)
                        FROM BookingPayment p
                        WHERE p.status = :status
                        """)
        BigDecimal getTotalPaymentsByStatus(
                        @Param("status") String status);

        // Payment totals for owner dashboard
        @Query("""
                        SELECT COALESCE(SUM(p.amount), 0)
                        FROM BookingPayment p
                        WHERE p.status = :status
                          AND p.booking.farmhouse.owner.id = :ownerId
                        """)
        BigDecimal getOwnerTotalPaymentsByStatus(
                        @Param("ownerId") Long ownerId,
                        @Param("status") String status);

        long countByStatus(String status);

        long countByBooking_Farmhouse_Owner_Id(Long ownerId);

        long countByBooking_Farmhouse_Owner_IdAndStatus(
                        Long ownerId,
                        String status);
}