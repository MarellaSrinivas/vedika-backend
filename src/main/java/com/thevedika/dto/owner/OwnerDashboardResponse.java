package com.thevedika.dto.owner;

import java.math.BigDecimal;

public record OwnerDashboardResponse(
        long totalFarmhouses,
        long activeFarmhouses,
        long inactiveFarmhouses,

        long totalBookings,
        long pendingBookings,
        long confirmedBookings,
        long completedBookings,
        long cancelledBookings,

        long totalPaymentRecords,
        long successfulPayments,

        BigDecimal totalRevenue) {
}