package com.thevedika.service;

import com.thevedika.dto.AvailabilityResponse;
import com.thevedika.dto.FarmhouseSearchResponse;
import com.thevedika.dto.FarmhouseCalendarDayResponse;

import java.time.LocalDate;
import java.util.List;

public interface FarmhouseAvailabilityService {

        AvailabilityResponse getAvailability(
                        Long farmhouseId,
                        LocalDate checkIn,
                        LocalDate checkOut,
                        Integer guests);

        List<FarmhouseSearchResponse> search(
                        String location,
                        LocalDate checkIn,
                        LocalDate checkOut,
                        Integer guests);

        List<FarmhouseCalendarDayResponse> getCalendar(Long farmhouseId);
}