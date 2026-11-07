package com.thevedika.controller;

import com.thevedika.entity.Amenity;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAmenity;
import com.thevedika.repository.AmenityRepository;
import com.thevedika.repository.FarmhouseAmenityRepository;
import com.thevedika.repository.FarmhouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/farmhouses/{farmhouseId}/amenities")
public class FarmhouseAmenityController {

    private final FarmhouseRepository farmhouseRepository;
    private final AmenityRepository amenityRepository;
    private final FarmhouseAmenityRepository farmhouseAmenityRepository;

    public FarmhouseAmenityController(
            FarmhouseRepository farmhouseRepository,
            AmenityRepository amenityRepository,
            FarmhouseAmenityRepository farmhouseAmenityRepository) {
        this.farmhouseRepository = farmhouseRepository;
        this.amenityRepository = amenityRepository;
        this.farmhouseAmenityRepository = farmhouseAmenityRepository;
    }

    @GetMapping
    public List<FarmhouseAmenity> getAmenities(
            @PathVariable Long farmhouseId) {
        return farmhouseAmenityRepository.findByFarmhouseId(farmhouseId);
    }

    @PostMapping("/{amenityId}")
    public FarmhouseAmenity addAmenity(
            @PathVariable Long farmhouseId,
            @PathVariable Long amenityId) {
        Farmhouse farmhouse = farmhouseRepository.findById(farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));

        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Amenity not found"));

        FarmhouseAmenity farmhouseAmenity = new FarmhouseAmenity();
        farmhouseAmenity.setFarmhouse(farmhouse);
        farmhouseAmenity.setAmenity(amenity);

        return farmhouseAmenityRepository.save(farmhouseAmenity);
    }
}