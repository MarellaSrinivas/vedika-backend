package com.thevedika.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_dates", columnList = "farmhouse_id, check_in, check_out"),
        @Index(name = "idx_booking_customer", columnList = "customer_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_booking_reference", columnNames = "booking_reference")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_reference", nullable = false, unique = true, length = 40)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmhouse_id", nullable = false)
    private Farmhouse farmhouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Integer guests;

    @Column(name = "total_nights", nullable = false)
    private Integer totalNights;

    @Column(name = "accommodation_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal accommodationTotal;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "amount_paid", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Column(name = "balance_due", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceDue;

    @Column(name = "payment_option", nullable = false, length = 20)
    private String paymentOption;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "UNPAID";

    @Column(name = "booking_status", nullable = false, length = 30)
    private String bookingStatus = "PENDING_PAYMENT";

    @Column(name = "special_requests", length = 1000)
    private String specialRequests;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String value) {
        this.bookingReference = value;
    }

    public Farmhouse getFarmhouse() {
        return farmhouse;
    }

    public void setFarmhouse(Farmhouse value) {
        this.farmhouse = value;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User value) {
        this.customer = value;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate value) {
        this.checkIn = value;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate value) {
        this.checkOut = value;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer value) {
        this.guests = value;
    }

    public Integer getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(Integer value) {
        this.totalNights = value;
    }

    public BigDecimal getAccommodationTotal() {
        return accommodationTotal;
    }

    public void setAccommodationTotal(BigDecimal value) {
        this.accommodationTotal = value;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal value) {
        this.totalAmount = value;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal value) {
        this.amountPaid = value;
    }

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(BigDecimal value) {
        this.balanceDue = value;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String value) {
        this.paymentOption = value;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String value) {
        this.paymentStatus = value;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String value) {
        this.bookingStatus = value;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String value) {
        this.specialRequests = value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Column(name = "addons_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal addonsTotal = BigDecimal.ZERO;

    public BigDecimal getAddonsTotal() {
        return addonsTotal;
    }

    public void setAddonsTotal(BigDecimal addonsTotal) {
        this.addonsTotal = addonsTotal;
    }
}