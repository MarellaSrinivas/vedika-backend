package com.thevedika.dto.booking;

import com.thevedika.entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {

    private final String bookingReference;
    private final Long farmhouseId;
    private final String farmhouseName;
    private final String location;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final Integer guests;
    private final Integer nights;
    private final BigDecimal accommodationTotal;
    private final BigDecimal totalAmount;
    private final BigDecimal amountPaid;
    private final BigDecimal balanceDue;
    private final String paymentOption;
    private final String paymentStatus;
    private final String bookingStatus;
    private final String specialRequests;
    private final LocalDateTime createdAt;
    private BigDecimal addonsTotal;

    public BookingResponse(Booking booking) {
        this.bookingReference = booking.getBookingReference();
        this.farmhouseId = booking.getFarmhouse().getId();
        this.farmhouseName = booking.getFarmhouse().getName();
        this.location = booking.getFarmhouse().getLocation();
        this.checkIn = booking.getCheckIn();
        this.checkOut = booking.getCheckOut();
        this.guests = booking.getGuests();
        this.nights = booking.getTotalNights();
        this.accommodationTotal = booking.getAccommodationTotal();
        this.totalAmount = booking.getTotalAmount();
        this.amountPaid = booking.getAmountPaid();
        this.balanceDue = booking.getBalanceDue();
        this.paymentOption = booking.getPaymentOption();
        this.paymentStatus = booking.getPaymentStatus();
        this.bookingStatus = booking.getBookingStatus();
        this.specialRequests = booking.getSpecialRequests();
        this.createdAt = booking.getCreatedAt();
        this.addonsTotal = booking.getAddonsTotal() == null
                ? BigDecimal.ZERO
                : booking.getAddonsTotal();
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public Long getFarmhouseId() {
        return farmhouseId;
    }

    public String getFarmhouseName() {
        return farmhouseName;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public Integer getGuests() {
        return guests;
    }

    public Integer getNights() {
        return nights;
    }

    public BigDecimal getAccommodationTotal() {
        return accommodationTotal;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getAddonsTotal() {
        return addonsTotal;
    }

}