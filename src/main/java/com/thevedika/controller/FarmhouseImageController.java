package com.thevedika.controller;

import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseImage;
import com.thevedika.repository.FarmhouseImageRepository;
import com.thevedika.repository.FarmhouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/farmhouses/{farmhouseId}/images")
public class FarmhouseImageController {

    private final FarmhouseRepository farmhouseRepository;
    private final FarmhouseImageRepository imageRepository;

    public FarmhouseImageController(
            FarmhouseRepository farmhouseRepository,
            FarmhouseImageRepository imageRepository) {
        this.farmhouseRepository = farmhouseRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public List<FarmhouseImage> getImages(
            @PathVariable Long farmhouseId) {
        return imageRepository
                .findByFarmhouseIdOrderByDisplayOrderAsc(farmhouseId);
    }

    @PostMapping
    public FarmhouseImage addImage(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseImage image) {
        Farmhouse farmhouse = farmhouseRepository.findById(farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));

        image.setFarmhouse(farmhouse);

        return imageRepository.save(image);
    }
}