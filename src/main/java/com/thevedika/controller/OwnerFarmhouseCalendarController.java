
package com.thevedika.controller;

import com.thevedika.dto.AvailabilityDateRequest;
import com.thevedika.dto.SpecialPriceRequest;
import com.thevedika.dto.owner.OwnerCalendarDayResponse;
import com.thevedika.service.FarmhouseCalendarManagementService;
import com.thevedika.dto.owner.OwnerCalendarDayResponse;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/owner/farmhouses/{farmhouseId}/calendar")
public class OwnerFarmhouseCalendarController {

    private final FarmhouseCalendarManagementService calendarService;

    public OwnerFarmhouseCalendarController(
            FarmhouseCalendarManagementService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public List<OwnerCalendarDayResponse> getCalendar(
            @PathVariable Long farmhouseId,
            Authentication authentication) {

        return calendarService.getOwnerCalendar(
                authentication.getName(),
                farmhouseId);
    }

    @PutMapping("/price")
    public String setSpecialPrice(
            @PathVariable Long farmhouseId,
            @RequestBody SpecialPriceRequest request,
            Authentication authentication) {
        calendarService.setOwnerSpecialPrice(
                authentication.getName(),
                farmhouseId,
                request);

        return "Special price saved successfully";
    }

    @DeleteMapping("/price")
    public String removeSpecialPrice(
            @PathVariable Long farmhouseId,
            @RequestParam LocalDate date,
            Authentication authentication) {
        calendarService.removeOwnerSpecialPrice(
                authentication.getName(),
                farmhouseId,
                date);

        return "Special price removed successfully";
    }

    @PutMapping("/availability")
    public String updateAvailability(
            @PathVariable Long farmhouseId,
            @RequestBody AvailabilityDateRequest request,
            Authentication authentication) {
        calendarService.setOwnerAvailability(
                authentication.getName(),
                farmhouseId,
                request);

        return "Availability updated successfully";
    }
}
