package com.thevedika.dto.owner;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OwnerCalendarDayResponse(
        LocalDate date,
        String dayOfWeek,
        BigDecimal price,
        String pricingType,
        String status,
        Boolean available) {
}