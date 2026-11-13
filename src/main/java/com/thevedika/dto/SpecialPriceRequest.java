
package com.thevedika.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SpecialPriceRequest {

    private LocalDate priceDate;
    private BigDecimal price;

    public LocalDate getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(LocalDate priceDate) {
        this.priceDate = priceDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
