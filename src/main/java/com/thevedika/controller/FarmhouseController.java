
package com.thevedika.controller;

import com.thevedika.dto.FarmhouseCalendarDayResponse;
import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.service.FarmhouseResponseMapper;
import com.thevedika.service.FarmhouseService;
import com.thevedika.service.FarmhouseAvailabilityService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmhouses")
public class FarmhouseController {

    private final FarmhouseService farmhouseService;
    private final FarmhouseResponseMapper responseMapper;
    private final FarmhouseAvailabilityService farmhouseAvailabilityService;

    public FarmhouseController(
            FarmhouseService farmhouseService,
            FarmhouseResponseMapper responseMapper,
            FarmhouseAvailabilityService farmhouseAvailabilityService) {
        this.farmhouseService = farmhouseService;
        this.responseMapper = responseMapper;
        this.farmhouseAvailabilityService = farmhouseAvailabilityService;
    }

    @GetMapping
    public List<FarmhouseResponse> getAllFarmhouses() {

        return farmhouseService.getAllFarmhouses()
                .stream()
                .map(responseMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public FarmhouseResponse getFarmhouseById(
            @PathVariable Long id) {
        Farmhouse farmhouse = farmhouseService.getFarmhouseById(id);

        return responseMapper.toResponse(farmhouse);
    }

    @GetMapping("/{farmhouseId}/calendar")
    public List<FarmhouseCalendarDayResponse> getCalendar(
            @PathVariable Long farmhouseId) {

        return farmhouseAvailabilityService.getCalendar(farmhouseId);
    }
}
