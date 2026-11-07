package com.thevedika.controller;

import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAddon;
import com.thevedika.repository.FarmhouseAddonRepository;
import com.thevedika.repository.FarmhouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/farmhouses/{farmhouseId}/addons")
public class FarmhouseAddonController {

    private final FarmhouseRepository farmhouseRepository;
    private final FarmhouseAddonRepository addonRepository;

    public FarmhouseAddonController(
            FarmhouseRepository farmhouseRepository,
            FarmhouseAddonRepository addonRepository) {
        this.farmhouseRepository = farmhouseRepository;
        this.addonRepository = addonRepository;
    }

    @GetMapping
    public List<FarmhouseAddon> getAddons(
            @PathVariable Long farmhouseId) {
        return addonRepository
                .findByFarmhouseIdAndAvailableTrue(farmhouseId);
    }

    @PostMapping
    public FarmhouseAddon addAddon(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseAddon addon) {
        Farmhouse farmhouse = farmhouseRepository.findById(farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));

        addon.setFarmhouse(farmhouse);

        return addonRepository.save(addon);
    }
}