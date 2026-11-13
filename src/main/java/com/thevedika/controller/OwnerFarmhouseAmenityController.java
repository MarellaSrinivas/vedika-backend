
package com.thevedika.controller;

import com.thevedika.dto.AmenityResponse;
import com.thevedika.dto.FarmhouseAmenitiesRequest;
import com.thevedika.service.FarmhouseAmenityService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/farmhouses/{farmhouseId}/amenities")
public class OwnerFarmhouseAmenityController {

    private final FarmhouseAmenityService amenityService;

    public OwnerFarmhouseAmenityController(
            FarmhouseAmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public ResponseEntity<List<AmenityResponse>> getAmenities(
            @PathVariable Long farmhouseId,
            Authentication authentication) {
        return ResponseEntity.ok(
                amenityService.getOwnerFarmhouseAmenities(
                        authentication.getName(),
                        farmhouseId));
    }

    @PutMapping
    public ResponseEntity<List<AmenityResponse>> updateAmenities(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseAmenitiesRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                amenityService.updateOwnerFarmhouseAmenities(
                        authentication.getName(),
                        farmhouseId,
                        request));
    }
}
