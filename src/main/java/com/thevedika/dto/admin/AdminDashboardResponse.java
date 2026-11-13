package com.thevedika.dto.admin;

import java.math.BigDecimal;

public class AdminDashboardResponse {

    private long totalCustomers;
    private long totalOwners;
    private long pendingOwners;

    private long totalFarmhouses;
    private long activeFarmhouses;
    private long inactiveFarmhouses;

    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private long cancelledBookings;
    private long completedBookings;

    private long totalPayments;
    private BigDecimal successfulPaymentRevenue;

    public AdminDashboardResponse() {
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(long totalOwners) {
        this.totalOwners = totalOwners;
    }

    public long getPendingOwners() {
        return pendingOwners;
    }

    public void setPendingOwners(long pendingOwners) {
        this.pendingOwners = pendingOwners;
    }

    public long getTotalFarmhouses() {
        return totalFarmhouses;
    }

    public void setTotalFarmhouses(long totalFarmhouses) {
        this.totalFarmhouses = totalFarmhouses;
    }

    public long getActiveFarmhouses() {
        return activeFarmhouses;
    }

    public void setActiveFarmhouses(long activeFarmhouses) {
        this.activeFarmhouses = activeFarmhouses;
    }

    public long getInactiveFarmhouses() {
        return inactiveFarmhouses;
    }

    public void setInactiveFarmhouses(long inactiveFarmhouses) {
        this.inactiveFarmhouses = inactiveFarmhouses;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public long getConfirmedBookings() {
        return confirmedBookings;
    }

    public void setConfirmedBookings(long confirmedBookings) {
        this.confirmedBookings = confirmedBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }

    public long getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(long completedBookings) {
        this.completedBookings = completedBookings;
    }

    public long getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(long totalPayments) {
        this.totalPayments = totalPayments;
    }

    public BigDecimal getSuccessfulPaymentRevenue() {
        return successfulPaymentRevenue;
    }

    public void setSuccessfulPaymentRevenue(
            BigDecimal successfulPaymentRevenue) {
        this.successfulPaymentRevenue = successfulPaymentRevenue;
    }
}