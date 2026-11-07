package com.thevedika.service.impl;

import com.thevedika.dto.AvailabilityResponse;
import com.thevedika.dto.FarmhouseSearchResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAvailability;
import com.thevedika.entity.FarmhouseImage;
import com.thevedika.repository.FarmhouseAvailabilityRepository;
import com.thevedika.repository.FarmhouseImageRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.FarmhouseSpecialPriceRepository;
import com.thevedika.service.FarmhouseAvailabilityService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FarmhouseAvailabilityServiceImpl
        implements FarmhouseAvailabilityService {

    private final FarmhouseRepository farmhouseRepository;
    private final FarmhouseAvailabilityRepository availabilityRepository;
    private final FarmhouseSpecialPriceRepository specialPriceRepository;
    private final FarmhouseImageRepository imageRepository;

    public FarmhouseAvailabilityServiceImpl(
            FarmhouseRepository farmhouseRepository,
            FarmhouseAvailabilityRepository availabilityRepository,
            FarmhouseSpecialPriceRepository specialPriceRepository,
            FarmhouseImageRepository imageRepository) {

        this.farmhouseRepository = farmhouseRepository;
        this.availabilityRepository = availabilityRepository;
        this.specialPriceRepository = specialPriceRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public AvailabilityResponse getAvailability(
            Long farmhouseId,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer guests) {

        Farmhouse farmhouse = getActiveFarmhouse(farmhouseId);

        validateDates(checkIn, checkOut);
        validateGuests(farmhouse, guests);

        List<AvailabilityResponse.NightlyPrice> nightlyPrices = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;
        boolean available = true;

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {

            if (!isDateAvailable(farmhouseId, date)) {
                available = false;
            }

            BigDecimal nightlyPrice = calculateNightlyPrice(farmhouse, date, guests);

            nightlyPrices.add(
                    new AvailabilityResponse.NightlyPrice(
                            date, nightlyPrice));

            total = total.add(nightlyPrice);
        }

        AvailabilityResponse response = new AvailabilityResponse();
        response.setFarmhouseId(farmhouseId);
        response.setCheckIn(checkIn);
        response.setCheckOut(checkOut);
        response.setGuests(guests);
        response.setNights(
                (int) ChronoUnit.DAYS.between(checkIn, checkOut));
        response.setAvailable(available);
        response.setAccommodationTotal(total);
        response.setNightlyPrices(nightlyPrices);

        return response;
    }

    @Override
    public List<FarmhouseSearchResponse> search(
            String location,
            LocalDate checkIn,
            LocalDate checkOut,
            Integer guests) {

        validateDates(checkIn, checkOut);

        if (guests == null || guests < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Guests must be at least 1");
        }

        List<FarmhouseSearchResponse> results = new ArrayList<>();

        for (Farmhouse farmhouse : farmhouseRepository.findByActiveTrue()) {

            if (location != null && !location.isBlank()
                    && !farmhouse.getLocation()
                            .toLowerCase()
                            .contains(location.trim().toLowerCase())) {
                continue;
            }

            if (guests < farmhouse.getMinimumGuests()
                    || guests > farmhouse.getMaximumGuests()) {
                continue;
            }

            AvailabilityResponse availability = getAvailability(
                    farmhouse.getId(),
                    checkIn,
                    checkOut,
                    guests);

            if (!availability.isAvailable()) {
                continue;
            }

            FarmhouseSearchResponse response = new FarmhouseSearchResponse();

            response.setId(farmhouse.getId());
            response.setName(farmhouse.getName());
            response.setLocation(farmhouse.getLocation());
            response.setDescription(farmhouse.getDescription());
            response.setBasePrice(farmhouse.getBasePrice());
            response.setWeekendPrice(farmhouse.getWeekendPrice());
            response.setExtraGuestPrice(farmhouse.getExtraGuestPrice());
            response.setMinimumGuests(farmhouse.getMinimumGuests());
            response.setMaximumGuests(farmhouse.getMaximumGuests());
            response.setBedrooms(farmhouse.getBedrooms());
            response.setBathrooms(farmhouse.getBathrooms());
            response.setRating(farmhouse.getRating());
            response.setReviewCount(farmhouse.getReviewCount());
            response.setCheckInTime(farmhouse.getCheckInTime());
            response.setCheckOutTime(farmhouse.getCheckOutTime());
            response.setAvailable(true);
            response.setTotalPrice(
                    availability.getAccommodationTotal());

            List<FarmhouseImage> images = imageRepository
                    .findByFarmhouseIdOrderByDisplayOrderAsc(
                            farmhouse.getId());

            if (!images.isEmpty()) {
                response.setImageUrl(
                        images.get(0).getImageUrl());
            }

            results.add(response);
        }

        return results;
    }

    private Farmhouse getActiveFarmhouse(Long id) {
        return farmhouseRepository.findById(id)
                .filter(farmhouse -> Boolean.TRUE.equals(farmhouse.getActive()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));
    }

    private boolean isDateAvailable(
            Long farmhouseId, LocalDate date) {

        return availabilityRepository
                .findByFarmhouseIdAndAvailabilityDate(
                        farmhouseId, date)
                .map(FarmhouseAvailability::getAvailable)
                .orElse(true);
    }

    private BigDecimal calculateNightlyPrice(
            Farmhouse farmhouse,
            LocalDate date,
            Integer guests) {

        BigDecimal basePrice = specialPriceRepository
                .findByFarmhouseIdAndPriceDate(
                        farmhouse.getId(), date)
                .map(price -> price.getPrice())
                .orElseGet(() -> {
                    DayOfWeek day = date.getDayOfWeek();

                    if (day == DayOfWeek.FRIDAY
                            || day == DayOfWeek.SATURDAY) {
                        return farmhouse.getWeekendPrice();
                    }

                    return farmhouse.getBasePrice();
                });

        int extraGuests = Math.max(
                0, guests - farmhouse.getMinimumGuests());

        BigDecimal extraGuestCost = farmhouse.getExtraGuestPrice()
                .multiply(BigDecimal.valueOf(extraGuests));

        return basePrice.add(extraGuestCost);
    }

    private void validateDates(
            LocalDate checkIn, LocalDate checkOut) {

        if (checkIn == null || checkOut == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Check-in and check-out dates are required");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Check-out must be after check-in");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Check-in cannot be in the past");
        }
    }

    private void validateGuests(
            Farmhouse farmhouse, Integer guests) {

        if (guests == null
                || guests < farmhouse.getMinimumGuests()
                || guests > farmhouse.getMaximumGuests()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Guest count must be between "
                            + farmhouse.getMinimumGuests()
                            + " and "
                            + farmhouse.getMaximumGuests());
        }
    }
}