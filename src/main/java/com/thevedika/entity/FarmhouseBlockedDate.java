
package com.thevedika.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "farmhouse_blocked_dates", uniqueConstraints = {
        @UniqueConstraint(name = "uk_farmhouse_blocked_date", columnNames = { "farmhouse_id", "blocked_date" })
})
public class FarmhouseBlockedDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmhouse_id", nullable = false)
    private Farmhouse farmhouse;

    @Column(name = "blocked_date", nullable = false)
    private LocalDate blockedDate;

    @Column(length = 300)
    private String reason;

    public FarmhouseBlockedDate() {
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

    public LocalDate getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(LocalDate blockedDate) {
        this.blockedDate = blockedDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
