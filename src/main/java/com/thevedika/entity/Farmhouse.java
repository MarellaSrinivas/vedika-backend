package com.thevedika.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "farmhouses")
public class Farmhouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal weekendPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal extraGuestPrice;

    @Column(nullable = false)
    private Integer minimumGuests;

    @Column(nullable = false)
    private Integer maximumGuests;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer bathrooms;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false, length = 50)
    private String checkInTime;

    @Column(nullable = false, length = 50)
    private String checkOutTime;

    @Column(nullable = false)
    private Boolean active = true;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // Property information
    @Column(length = 50)
    private String propertyExperience = "PRIVATE_STAY";

    @Column(length = 255)
    private String addressLine;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 10)
    private String pincode;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    // Property rules
    @Column(columnDefinition = "TEXT")
    private String houseRules;

    @Column(columnDefinition = "TEXT")
    private String eventsPolicy;

    @Column(columnDefinition = "TEXT")
    private String cookingFoodPolicy;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    public Farmhouse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getWeekendPrice() {
        return weekendPrice;
    }

    public void setWeekendPrice(BigDecimal weekendPrice) {
        this.weekendPrice = weekendPrice;
    }

    public BigDecimal getExtraGuestPrice() {
        return extraGuestPrice;
    }

    public void setExtraGuestPrice(BigDecimal extraGuestPrice) {
        this.extraGuestPrice = extraGuestPrice;
    }

    public Integer getMinimumGuests() {
        return minimumGuests;
    }

    public void setMinimumGuests(Integer minimumGuests) {
        this.minimumGuests = minimumGuests;
    }

    public Integer getMaximumGuests() {
        return maximumGuests;
    }

    public void setMaximumGuests(Integer maximumGuests) {
        this.maximumGuests = maximumGuests;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPropertyExperience() {
        return propertyExperience;
    }

    public void setPropertyExperience(String propertyExperience) {
        this.propertyExperience = propertyExperience;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getHouseRules() {
        return houseRules;
    }

    public void setHouseRules(String houseRules) {
        this.houseRules = houseRules;
    }

    public String getEventsPolicy() {
        return eventsPolicy;
    }

    public void setEventsPolicy(String eventsPolicy) {
        this.eventsPolicy = eventsPolicy;
    }

    public String getCookingFoodPolicy() {
        return cookingFoodPolicy;
    }

    public void setCookingFoodPolicy(String cookingFoodPolicy) {
        this.cookingFoodPolicy = cookingFoodPolicy;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

}