
package com.thevedika.dto;

import java.math.BigDecimal;

public record FarmhouseAddonResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String pricingType,
        Boolean available) {
}
