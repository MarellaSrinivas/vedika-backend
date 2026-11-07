package com.thevedika.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "farmhouse_availability", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "farmhouse_id", "availability_date" })
})
public class FarmhouseAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmhouse_id", nullable = false)
    private Farmhouse farmhouse;

    @Column(name = "availability_date", nullable = false)
    private LocalDate availabilityDate;

    @Column(nullable = false)
    private Boolean available = true;

    public FarmhouseAvailability() {
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

    public LocalDate getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(LocalDate availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}