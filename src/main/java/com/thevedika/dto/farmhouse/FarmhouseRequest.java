package com.thevedika.dto.farmhouse;

import java.math.BigDecimal;

public class FarmhouseRequest {

    private String name;
    private String location;
    private String description;

    private BigDecimal basePrice;
    private BigDecimal weekendPrice;
    private BigDecimal extraGuestPrice;

    private Integer minimumGuests;
    private Integer maximumGuests;

    private Integer bedrooms;
    private Integer bathrooms;

    private String checkInTime;
    private String checkOutTime;

    private String propertyExperience;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String houseRules;
    private String eventsPolicy;
    private String cookingFoodPolicy;

    public String getName() {
        return name;
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
}