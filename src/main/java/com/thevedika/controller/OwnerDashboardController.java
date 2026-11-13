package com.thevedika.controller;

import com.thevedika.dto.owner.*;
import com.thevedika.service.OwnerDashboardService;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/owner/dashboard")
public class OwnerDashboardController {

    private final OwnerDashboardService ownerDashboardService;

    public OwnerDashboardController(
            OwnerDashboardService ownerDashboardService) {
        this.ownerDashboardService = ownerDashboardService;
    }

    @GetMapping
    public OwnerDashboardResponse getDashboard(
            Authentication authentication) {
        return ownerDashboardService.getDashboard(
                authentication.getName());
    }

    @GetMapping("/bookings")
    public List<OwnerBookingResponse> getBookings(
            Authentication authentication) {
        return ownerDashboardService.getBookings(
                authentication.getName());
    }

    @GetMapping("/payments")
    public List<OwnerPaymentResponse> getPayments(
            Authentication authentication) {
        return ownerDashboardService.getPayments(
                authentication.getName());
    }
}