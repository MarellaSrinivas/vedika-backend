package com.thevedika.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "farmhouse_special_prices", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "farmhouse_id", "price_date" })
})
public class FarmhouseSpecialPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmhouse_id", nullable = false)
    private Farmhouse farmhouse;

    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public FarmhouseSpecialPrice() {
    }

    public Long getId() {
        return id;
    }

    public Farmhouse getFarmhouse() {
        return farmhouse;
    }

    public void setFarmhouse(Farmhouse farmhouse) {
        this.farmhouse = farmhouse;
    }

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