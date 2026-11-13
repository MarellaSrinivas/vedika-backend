
package com.thevedika.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FarmhouseCalendarDayResponse(
        LocalDate date,
        String dayOfWeek,
        BigDecimal price,
        String pricingType,
        boolean available,
        String status) {
}
