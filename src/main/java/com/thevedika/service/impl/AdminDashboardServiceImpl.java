package com.thevedika.service.impl;

import com.thevedika.dto.admin.AdminDashboardResponse;
import com.thevedika.repository.UserRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.BookingRepository;
import com.thevedika.repository.BookingPaymentRepository;
import com.thevedika.service.AdminDashboardService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final UserRepository userRepository;
    private final FarmhouseRepository farmhouseRepository;
    private final BookingRepository bookingRepository;
    private final BookingPaymentRepository paymentRepository;

    public AdminDashboardServiceImpl(
            UserRepository userRepository,
            FarmhouseRepository farmhouseRepository,
            BookingRepository bookingRepository,
            BookingPaymentRepository paymentRepository) {

        this.userRepository = userRepository;
        this.farmhouseRepository = farmhouseRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse response = new AdminDashboardResponse();

        // Users
        response.setTotalCustomers(
                userRepository.countByRole("CUSTOMER"));

        response.setTotalOwners(
                userRepository.countByRole("OWNER"));

        response.setPendingOwners(
                userRepository.countByRoleAndApproved(
                        "OWNER", false));

        // Farmhouses
        response.setTotalFarmhouses(
                farmhouseRepository.count());

        response.setActiveFarmhouses(
                farmhouseRepository.countByActiveTrue());

        response.setInactiveFarmhouses(
                farmhouseRepository.countByActiveFalse());

        // Bookings
        response.setTotalBookings(
                bookingRepository.count());

        response.setPendingBookings(
                bookingRepository.countByBookingStatus(
                        "PENDING_PAYMENT"));

        response.setConfirmedBookings(
                bookingRepository.countByBookingStatus(
                        "CONFIRMED"));

        response.setCancelledBookings(
                bookingRepository.countByBookingStatus(
                        "CANCELLED"));

        response.setCompletedBookings(
                bookingRepository.countByBookingStatus(
                        "COMPLETED"));

        // Payments
        response.setTotalPayments(
                paymentRepository.count());

        response.setSuccessfulPaymentRevenue(
                paymentRepository.getTotalPaymentsByStatus(
                        "SUCCESS"));

        if (response.getSuccessfulPaymentRevenue() == null) {
            response.setSuccessfulPaymentRevenue(BigDecimal.ZERO);
        }

        return response;
    }
}