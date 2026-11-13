package com.thevedika.service.impl;

import com.thevedika.dto.owner.*;
import com.thevedika.entity.Booking;
import com.thevedika.entity.BookingPayment;
import com.thevedika.entity.User;
import com.thevedika.repository.BookingPaymentRepository;
import com.thevedika.repository.BookingRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.OwnerDashboardService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OwnerDashboardServiceImpl implements OwnerDashboardService {

    private static final String SUCCESS_PAYMENT_STATUS = "SUCCESS";

    private final UserRepository userRepository;
    private final FarmhouseRepository farmhouseRepository;
    private final BookingRepository bookingRepository;
    private final BookingPaymentRepository paymentRepository;

    public OwnerDashboardServiceImpl(
            UserRepository userRepository,
            FarmhouseRepository farmhouseRepository,
            BookingRepository bookingRepository,
            BookingPaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.farmhouseRepository = farmhouseRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    private User getApprovedOwner(String phone) {

        User owner = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner account not found"));

        if (!"OWNER".equals(owner.getRole())
                || !owner.isActive()
                || !owner.isApproved()) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Owner account is not active or approved");
        }

        return owner;
    }

    @Override
    public OwnerDashboardResponse getDashboard(String phone) {

        User owner = getApprovedOwner(phone);
        Long ownerId = owner.getId();

        long totalFarmhouses = farmhouseRepository.countByOwner_Id(ownerId);

        long activeFarmhouses = farmhouseRepository.countByOwner_IdAndActiveTrue(ownerId);

        long totalBookings = bookingRepository.countByFarmhouse_Owner_Id(ownerId);

        long pendingBookings = bookingRepository.countByFarmhouse_Owner_IdAndBookingStatus(
                ownerId, "PENDING_PAYMENT");

        long confirmedBookings = bookingRepository.countByFarmhouse_Owner_IdAndBookingStatus(
                ownerId, "CONFIRMED");

        long completedBookings = bookingRepository.countByFarmhouse_Owner_IdAndBookingStatus(
                ownerId, "COMPLETED");

        long cancelledBookings = bookingRepository.countByFarmhouse_Owner_IdAndBookingStatus(
                ownerId, "CANCELLED");

        long totalPaymentRecords = paymentRepository.countByBooking_Farmhouse_Owner_Id(ownerId);

        long successfulPayments = paymentRepository.countByBooking_Farmhouse_Owner_IdAndStatus(
                ownerId, SUCCESS_PAYMENT_STATUS);

        BigDecimal totalRevenue = paymentRepository.getOwnerTotalPaymentsByStatus(
                ownerId, SUCCESS_PAYMENT_STATUS);

        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        return new OwnerDashboardResponse(
                totalFarmhouses,
                activeFarmhouses,
                totalFarmhouses - activeFarmhouses,

                totalBookings,
                pendingBookings,
                confirmedBookings,
                completedBookings,
                cancelledBookings,

                totalPaymentRecords,
                successfulPayments,

                totalRevenue);
    }

    @Override
    public List<OwnerBookingResponse> getBookings(String phone) {

        User owner = getApprovedOwner(phone);

        List<Booking> bookings = bookingRepository.findByFarmhouse_Owner_IdOrderByCreatedAtDesc(
                owner.getId());

        return bookings.stream()
                .map(this::mapBooking)
                .toList();
    }

    private OwnerBookingResponse mapBooking(Booking booking) {

        return new OwnerBookingResponse(
                booking.getId(),
                booking.getBookingReference(),

                booking.getFarmhouse().getName(),

                booking.getCustomer().getFullName(),
                booking.getCustomer().getPhone(),

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

                booking.getCreatedAt());
    }

    @Override
    public List<OwnerPaymentResponse> getPayments(String phone) {

        User owner = getApprovedOwner(phone);

        List<BookingPayment> payments = paymentRepository
                .findByBooking_Farmhouse_Owner_IdOrderByCreatedAtDesc(
                        owner.getId());

        return payments.stream()
                .map(this::mapPayment)
                .toList();
    }

    private OwnerPaymentResponse mapPayment(BookingPayment payment) {

        Booking booking = payment.getBooking();

        return new OwnerPaymentResponse(
                payment.getId(),

                booking.getBookingReference(),
                booking.getFarmhouse().getName(),

                booking.getCustomer().getFullName(),
                booking.getCustomer().getPhone(),

                payment.getRazorpayOrderId(),
                payment.getRazorpayPaymentId(),

                payment.getAmount(),
                payment.getCurrency(),

                payment.getPaymentOption(),
                payment.getStatus(),

                payment.getCreatedAt());
    }
}