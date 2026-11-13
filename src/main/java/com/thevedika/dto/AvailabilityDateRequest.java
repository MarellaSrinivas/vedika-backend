
package com.thevedika.dto;

import java.time.LocalDate;

public class AvailabilityDateRequest {

    private LocalDate date;
    private Boolean available;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
