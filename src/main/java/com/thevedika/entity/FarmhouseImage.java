package com.thevedika.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "farmhouse_images")
public class FarmhouseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmhouse_id", nullable = false)
    private Farmhouse farmhouse;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean coverImage = false;

    public FarmhouseImage() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Boolean coverImage) {
        this.coverImage = coverImage;
    }
}