
package com.thevedika.service;

import com.thevedika.dto.AvailabilityDateRequest;
import com.thevedika.dto.SpecialPriceRequest;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAvailability;
import com.thevedika.entity.FarmhouseSpecialPrice;
import com.thevedika.entity.User;
import com.thevedika.repository.FarmhouseAvailabilityRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.FarmhouseSpecialPriceRepository;
import com.thevedika.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.thevedika.dto.owner.OwnerCalendarDayResponse;
import com.thevedika.entity.Booking;
import com.thevedika.repository.BookingRepository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class FarmhouseCalendarManagementService {

        private final FarmhouseRepository farmhouseRepository;
        private final FarmhouseAvailabilityRepository availabilityRepository;
        private final FarmhouseSpecialPriceRepository specialPriceRepository;
        private final UserRepository userRepository;
        private final BookingRepository bookingRepository;

        public FarmhouseCalendarManagementService(
                        FarmhouseRepository farmhouseRepository,
                        FarmhouseAvailabilityRepository availabilityRepository,
                        FarmhouseSpecialPriceRepository specialPriceRepository,
                        UserRepository userRepository,
                        BookingRepository bookingRepository) {
                this.farmhouseRepository = farmhouseRepository;
                this.availabilityRepository = availabilityRepository;
                this.specialPriceRepository = specialPriceRepository;
                this.userRepository = userRepository;
                this.bookingRepository = bookingRepository;
        }

        // ADMIN METHODS

        @Transactional
        public FarmhouseSpecialPrice setAdminSpecialPrice(
                        Long farmhouseId,
                        SpecialPriceRequest request) {
                Farmhouse farmhouse = getFarmhouse(farmhouseId);
                return saveSpecialPrice(farmhouseId, farmhouse, request);
        }

        @Transactional
        public void removeAdminSpecialPrice(
                        Long farmhouseId,
                        LocalDate date) {
                getFarmhouse(farmhouseId);
                validateFutureDate(date);

                specialPriceRepository.deleteByFarmhouseIdAndPriceDate(
                                farmhouseId, date);
        }

        @Transactional
        public FarmhouseAvailability setAdminAvailability(
                        Long farmhouseId,
                        AvailabilityDateRequest request) {
                Farmhouse farmhouse = getFarmhouse(farmhouseId);
                return saveAvailability(farmhouseId, farmhouse, request);
        }

        // OWNER METHODS

        @Transactional
        public FarmhouseSpecialPrice setOwnerSpecialPrice(
                        String phone,
                        Long farmhouseId,
                        SpecialPriceRequest request) {
                Farmhouse farmhouse = verifyOwner(phone, farmhouseId);
                return saveSpecialPrice(farmhouseId, farmhouse, request);
        }

        @Transactional
        public void removeOwnerSpecialPrice(
                        String phone,
                        Long farmhouseId,
                        LocalDate date) {
                verifyOwner(phone, farmhouseId);
                validateFutureDate(date);

                specialPriceRepository.deleteByFarmhouseIdAndPriceDate(
                                farmhouseId, date);
        }

        @Transactional
        public FarmhouseAvailability setOwnerAvailability(
                        String phone,
                        Long farmhouseId,
                        AvailabilityDateRequest request) {
                Farmhouse farmhouse = verifyOwner(phone, farmhouseId);
                return saveAvailability(farmhouseId, farmhouse, request);
        }

        // COMMON METHODS

        private Farmhouse getFarmhouse(Long farmhouseId) {
                return farmhouseRepository.findById(farmhouseId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Farmhouse not found"));
        }

        private Farmhouse verifyOwner(
                        String phone,
                        Long farmhouseId) {
                User owner = userRepository.findByPhone(phone)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Owner not found"));

                return farmhouseRepository.findByIdAndOwner_Id(
                                farmhouseId, owner.getId()).orElseThrow(
                                                () -> new ResponseStatusException(
                                                                HttpStatus.FORBIDDEN,
                                                                "You do not own this farmhouse"));
        }

        private FarmhouseSpecialPrice saveSpecialPrice(
                        Long farmhouseId,
                        Farmhouse farmhouse,
                        SpecialPriceRequest request) {
                if (request == null || request.getPriceDate() == null) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Price date is required");
                }

                validateFutureDate(request.getPriceDate());

                if (request.getPrice() == null ||
                                request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Special price must be greater than zero");
                }

                FarmhouseSpecialPrice specialPrice = specialPriceRepository
                                .findByFarmhouseIdAndPriceDate(
                                                farmhouseId,
                                                request.getPriceDate())
                                .orElseGet(FarmhouseSpecialPrice::new);

                specialPrice.setFarmhouse(farmhouse);
                specialPrice.setPriceDate(request.getPriceDate());
                specialPrice.setPrice(request.getPrice());

                return specialPriceRepository.save(specialPrice);
        }

        private FarmhouseAvailability saveAvailability(
                        Long farmhouseId,
                        Farmhouse farmhouse,
                        AvailabilityDateRequest request) {
                if (request == null || request.getDate() == null) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Availability date is required");
                }

                validateFutureDate(request.getDate());

                if (request.getAvailable() == null) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Available must be true or false");
                }

                FarmhouseAvailability availability = availabilityRepository
                                .findByFarmhouseIdAndAvailabilityDate(
                                                farmhouseId,
                                                request.getDate())
                                .orElseGet(FarmhouseAvailability::new);

                availability.setFarmhouse(farmhouse);
                availability.setAvailabilityDate(request.getDate());
                availability.setAvailable(request.getAvailable());

                return availabilityRepository.save(availability);
        }

        private void validateFutureDate(LocalDate date) {
                if (date == null || date.isBefore(LocalDate.now())) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Date cannot be in the past");
                }
        }

        @Transactional(readOnly = true)
        public List<OwnerCalendarDayResponse> getOwnerCalendar(
                        String phone,
                        Long farmhouseId) {

                Farmhouse farmhouse = verifyOwner(phone, farmhouseId);

                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(29);

                List<FarmhouseAvailability> availabilityList = availabilityRepository
                                .findByFarmhouseIdAndAvailabilityDateBetween(
                                                farmhouseId, startDate, endDate);

                List<FarmhouseSpecialPrice> specialPriceList = specialPriceRepository
                                .findByFarmhouseIdAndPriceDateBetweenOrderByPriceDateAsc(
                                                farmhouseId, startDate, endDate);

                List<Booking> bookings = bookingRepository
                                .findByFarmhouseIdAndBookingStatusInAndCheckInLessThanAndCheckOutGreaterThan(
                                                farmhouseId,
                                                List.of("CONFIRMED", "PENDING_PAYMENT"),
                                                endDate.plusDays(1),
                                                startDate);

                Map<LocalDate, FarmhouseAvailability> availabilityMap = availabilityList.stream()
                                .collect(Collectors.toMap(
                                                FarmhouseAvailability::getAvailabilityDate,
                                                Function.identity()));

                Map<LocalDate, FarmhouseSpecialPrice> specialPriceMap = specialPriceList.stream()
                                .collect(Collectors.toMap(
                                                FarmhouseSpecialPrice::getPriceDate,
                                                Function.identity()));

                List<OwnerCalendarDayResponse> result = new ArrayList<>();

                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

                        LocalDate currentDate = date;

                        boolean booked = bookings.stream()
                                        .anyMatch(booking -> !currentDate.isBefore(booking.getCheckIn())
                                                        && currentDate.isBefore(booking.getCheckOut()));

                        FarmhouseAvailability availability = availabilityMap.get(date);

                        boolean available = availability == null
                                        || Boolean.TRUE.equals(availability.getAvailable());

                        FarmhouseSpecialPrice specialPrice = specialPriceMap.get(date);

                        BigDecimal price;
                        String pricingType;

                        if (specialPrice != null) {
                                price = specialPrice.getPrice();
                                pricingType = "SPECIAL";
                        } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY
                                        || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                price = farmhouse.getWeekendPrice();
                                pricingType = "WEEKEND";
                        } else {
                                price = farmhouse.getBasePrice();
                                pricingType = "BASE";
                        }

                        String status;

                        if (booked) {
                                status = "BOOKED";
                        } else if (!available) {
                                status = "BLOCKED";
                        } else {
                                status = "AVAILABLE";
                        }

                        result.add(new OwnerCalendarDayResponse(
                                        date,
                                        date.getDayOfWeek().toString(),
                                        price,
                                        pricingType,
                                        status,
                                        !booked && available));
                }

                return result;
        }
}
