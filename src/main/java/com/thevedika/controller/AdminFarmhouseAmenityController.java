
package com.thevedika.controller;

import com.thevedika.dto.AmenityResponse;
import com.thevedika.dto.FarmhouseAmenitiesRequest;
import com.thevedika.service.FarmhouseAmenityService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/farmhouses/{farmhouseId}/amenities")
public class AdminFarmhouseAmenityController {

    private final FarmhouseAmenityService amenityService;

    public AdminFarmhouseAmenityController(
            FarmhouseAmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<AmenityResponse>> getAmenities(
            @PathVariable Long farmhouseId) {
        return ResponseEntity.ok(
                amenityService.getAdminFarmhouseAmenities(farmhouseId));
    }

    @PutMapping
    public ResponseEntity<List<AmenityResponse>> updateAmenities(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseAmenitiesRequest request) {
        return ResponseEntity.ok(
                amenityService.updateAdminFarmhouseAmenities(
                        farmhouseId, request));
    }
}
