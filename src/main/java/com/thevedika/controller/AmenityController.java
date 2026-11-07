package com.thevedika.controller;

import com.thevedika.entity.Amenity;
import com.thevedika.repository.AmenityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/amenities")
public class AmenityController {

    private final AmenityRepository amenityRepository;

    public AmenityController(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @GetMapping
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @PostMapping
    public Amenity createAmenity(@RequestBody Amenity amenity) {
        return amenityRepository.save(amenity);
    }
}