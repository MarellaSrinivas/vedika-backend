package com.thevedika.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AvailabilityResponse {

    private Long farmhouseId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer guests;
    private Integer nights;

    private boolean available;

    private BigDecimal accommodationTotal;

    private List<NightlyPrice> nightlyPrices;

    public AvailabilityResponse() {
    }

    public Long getFarmhouseId() {
        return farmhouseId;
    }

    public void setFarmhouseId(Long farmhouseId) {
        this.farmhouseId = farmhouseId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BigDecimal getAccommodationTotal() {
        return accommodationTotal;
    }

    public void setAccommodationTotal(BigDecimal accommodationTotal) {
        this.accommodationTotal = accommodationTotal;
    }

    public List<NightlyPrice> getNightlyPrices() {
        return nightlyPrices;
    }

    public void setNightlyPrices(List<NightlyPrice> nightlyPrices) {
        this.nightlyPrices = nightlyPrices;
    }

    public static class NightlyPrice {

        private LocalDate date;
        private BigDecimal price;

        public NightlyPrice() {
        }

        public NightlyPrice(LocalDate date, BigDecimal price) {
            this.date = date;
            this.price = price;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}