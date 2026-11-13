package com.thevedika.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

public class CreateBookingRequest {

    @NotNull
    @Future
    private LocalDate checkIn;

    @NotNull
    @Future
    private LocalDate checkOut;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer guests;

    @Size(max = 1000)
    private String specialRequests;

    // Accepted values: FULL or ADVANCE.
    private String paymentOption = "FULL";

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

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String value) {
        this.specialRequests = value;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String value) {
        this.paymentOption = value;
    }

    @Size(max = 20, message = "You can select at most 20 add-ons")
    private List<Long> addonIds = new ArrayList<>();

    public List<Long> getAddonIds() {
        return addonIds;
    }

    public void setAddonIds(List<Long> addonIds) {
        this.addonIds = addonIds == null ? new ArrayList<>() : addonIds;
    }
}