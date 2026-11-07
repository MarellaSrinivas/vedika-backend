package com.thevedika.controller;

import com.thevedika.entity.Farmhouse;
import com.thevedika.service.FarmhouseService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmhouses")
public class FarmhouseController {

    private final FarmhouseService farmhouseService;

    public FarmhouseController(FarmhouseService farmhouseService) {
        this.farmhouseService = farmhouseService;
    }

    @GetMapping
    public List<Farmhouse> getAllFarmhouses() {
        return farmhouseService.getAllFarmhouses();
    }

    @GetMapping("/{id}")
    public Farmhouse getFarmhouseById(@PathVariable Long id) {
        return farmhouseService.getFarmhouseById(id);
    }

    @PostMapping
    public Farmhouse createFarmhouse(
            @RequestBody Farmhouse farmhouse) {
        return farmhouseService.createFarmhouse(farmhouse);
    }
}