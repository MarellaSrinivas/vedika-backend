
package com.thevedika.controller;

import com.thevedika.dto.AvailabilityDateRequest;
import com.thevedika.dto.SpecialPriceRequest;
import com.thevedika.service.FarmhouseCalendarManagementService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/farmhouses/{farmhouseId}/calendar")
public class AdminFarmhouseCalendarController {

    private final FarmhouseCalendarManagementService calendarService;

    public AdminFarmhouseCalendarController(
            FarmhouseCalendarManagementService calendarService) {
        this.calendarService = calendarService;
    }

    @PutMapping("/price")
    public String setSpecialPrice(
            @PathVariable Long farmhouseId,
            @RequestBody SpecialPriceRequest request) {
        calendarService.setAdminSpecialPrice(farmhouseId, request);
        return "Special price saved successfully";
    }

    @DeleteMapping("/price")
    public String removeSpecialPrice(
            @PathVariable Long farmhouseId,
            @RequestParam LocalDate date) {
        calendarService.removeAdminSpecialPrice(farmhouseId, date);
        return "Special price removed successfully";
    }

    @PutMapping("/availability")
    public String updateAvailability(
            @PathVariable Long farmhouseId,
            @RequestBody AvailabilityDateRequest request) {
        calendarService.setAdminAvailability(farmhouseId, request);
        return "Availability updated successfully";
    }
}
