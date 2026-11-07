package com.thevedika.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "farmhouse_addons")
public class FarmhouseAddon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmhouse_id", nullable = false)
    private Farmhouse farmhouse;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 30)
    private String pricingType;

    @Column(nullable = false)
    private Boolean available = true;

    public FarmhouseAddon() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPricingType() {
        return pricingType;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}