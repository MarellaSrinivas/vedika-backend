
package com.thevedika.dto;

import java.util.List;

public class FarmhouseAmenitiesRequest {

    private List<Long> amenityIds;

    public List<Long> getAmenityIds() {
        return amenityIds;
    }

    public void setAmenityIds(List<Long> amenityIds) {
        this.amenityIds = amenityIds;
    }
}
