package com.thevedika.service.impl;

import com.thevedika.dto.AvailabilityResponse;
import com.thevedika.dto.booking.BookingResponse;
import com.thevedika.dto.booking.CreateBookingRequest;
import com.thevedika.entity.Booking;
import com.thevedika.entity.BookingAddon;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAddon;
import com.thevedika.entity.User;
import com.thevedika.repository.BookingAddonRepository;
import com.thevedika.repository.BookingRepository;
import com.thevedika.repository.FarmhouseAddonRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.BookingService;
import com.thevedika.service.FarmhouseAvailabilityService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

        private static final List<String> BLOCKING_STATUSES = List.of("PENDING_PAYMENT", "CONFIRMED");

        private static final BigDecimal ZERO = BigDecimal.ZERO;

        private final BookingRepository bookingRepository;
        private final BookingAddonRepository bookingAddonRepository;
        private final FarmhouseAddonRepository farmhouseAddonRepository;
        private final FarmhouseRepository farmhouseRepository;
        private final UserRepository userRepository;
        private final FarmhouseAvailabilityService availabilityService;

        public BookingServiceImpl(
                        BookingRepository bookingRepository,
                        BookingAddonRepository bookingAddonRepository,
                        FarmhouseAddonRepository farmhouseAddonRepository,
                        FarmhouseRepository farmhouseRepository,
                        UserRepository userRepository,
                        FarmhouseAvailabilityService availabilityService) {

                this.bookingRepository = bookingRepository;
                this.bookingAddonRepository = bookingAddonRepository;
                this.farmhouseAddonRepository = farmhouseAddonRepository;
                this.farmhouseRepository = farmhouseRepository;
                this.userRepository = userRepository;
                this.availabilityService = availabilityService;
        }

        @Override
        @Transactional
        public BookingResponse createBooking(
                        Long farmhouseId,
                        String customerPhone,
                        CreateBookingRequest request) {

                validateRequest(request);

                Farmhouse farmhouse = farmhouseRepository
                                .findActiveByIdForUpdate(farmhouseId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Farmhouse not found"));

                User customer = userRepository.findByPhone(customerPhone)
                                .filter(User::isActive)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.UNAUTHORIZED,
                                                "Customer account not found or disabled"));

                int guests = request.getGuests();

                if (guests < farmhouse.getMinimumGuests()
                                || guests > farmhouse.getMaximumGuests()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Guest count must be between "
                                                        + farmhouse.getMinimumGuests() + " and "
                                                        + farmhouse.getMaximumGuests());
                }

                AvailabilityResponse availability = availabilityService.getAvailability(
                                farmhouseId,
                                request.getCheckIn(),
                                request.getCheckOut(),
                                guests);

                if (!availability.isAvailable()) {
                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Farmhouse is not available for the selected dates");
                }

                List<Booking> overlaps = bookingRepository
                                .findByFarmhouseIdAndBookingStatusInAndCheckInLessThanAndCheckOutGreaterThan(
                                                farmhouseId,
                                                BLOCKING_STATUSES,
                                                request.getCheckOut(),
                                                request.getCheckIn());

                if (!overlaps.isEmpty()) {
                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "These dates have already been booked");
                }

                BigDecimal accommodationTotal = availability.getAccommodationTotal();

                if (accommodationTotal == null
                                || accommodationTotal.compareTo(ZERO) <= 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Unable to calculate the accommodation price");
                }

                String paymentOption = normalizePaymentOption(request.getPaymentOption());

                int nights = Math.toIntExact(ChronoUnit.DAYS.between(
                                request.getCheckIn(), request.getCheckOut()));

                // Load available add-ons from the database.
                List<Long> selectedIds = request.getAddonIds();

                if (selectedIds == null) {
                        selectedIds = List.of();
                }

                Set<Long> uniqueIds = new HashSet<>(selectedIds);

                if (uniqueIds.size() != selectedIds.size()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Duplicate add-ons are not allowed");
                }

                List<FarmhouseAddon> availableAddons = farmhouseAddonRepository
                                .findByFarmhouseIdAndAvailableTrue(farmhouseId);

                Map<Long, FarmhouseAddon> addonMap = new HashMap<>();

                for (FarmhouseAddon addon : availableAddons) {
                        addonMap.put(addon.getId(), addon);
                }

                BigDecimal addonsTotal = ZERO;

                for (Long addonId : uniqueIds) {
                        FarmhouseAddon addon = addonMap.get(addonId);

                        if (addon == null) {
                                throw new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Invalid or unavailable add-on: " + addonId);
                        }

                        int quantity = getAddonQuantity(
                                        addon.getPricingType(), guests);

                        BigDecimal addonTotal = addon.getPrice()
                                        .multiply(BigDecimal.valueOf(quantity));

                        addonsTotal = addonsTotal.add(addonTotal);
                }

                BigDecimal totalAmount = accommodationTotal.add(addonsTotal);

                Booking booking = new Booking();

                booking.setBookingReference(generateBookingReference());
                booking.setFarmhouse(farmhouse);
                booking.setCustomer(customer);
                booking.setCheckIn(request.getCheckIn());
                booking.setCheckOut(request.getCheckOut());
                booking.setGuests(guests);
                booking.setTotalNights(nights);

                booking.setAccommodationTotal(accommodationTotal);
                booking.setAddonsTotal(addonsTotal);
                booking.setTotalAmount(totalAmount);

                booking.setAmountPaid(ZERO);
                booking.setBalanceDue(totalAmount);

                booking.setPaymentOption(paymentOption);
                booking.setPaymentStatus("UNPAID");
                booking.setBookingStatus("PENDING_PAYMENT");
                booking.setSpecialRequests(request.getSpecialRequests());

                Booking saved = bookingRepository.save(booking);

                // Save add-on price snapshots.
                for (Long addonId : uniqueIds) {

                        FarmhouseAddon addon = addonMap.get(addonId);

                        int quantity = getAddonQuantity(
                                        addon.getPricingType(), guests);

                        BigDecimal addonTotal = addon.getPrice()
                                        .multiply(BigDecimal.valueOf(quantity));

                        BookingAddon bookingAddon = new BookingAddon();

                        bookingAddon.setBooking(saved);
                        bookingAddon.setAddonId(addon.getId());
                        bookingAddon.setAddonName(addon.getName());
                        bookingAddon.setDescription(addon.getDescription());
                        bookingAddon.setUnitPrice(addon.getPrice());
                        bookingAddon.setPricingType(addon.getPricingType());
                        bookingAddon.setQuantity(quantity);
                        bookingAddon.setTotalPrice(addonTotal);

                        bookingAddonRepository.save(bookingAddon);
                }

                return new BookingResponse(saved);
        }

        private int getAddonQuantity(String pricingType, int guests) {

                if (pricingType == null || pricingType.isBlank()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Add-on pricing type is missing");
                }

                return switch (pricingType.trim().toUpperCase()) {
                        case "PER_STAY" -> 1;
                        case "PER_GUEST" -> guests;

                        // Backward compatibility with older database values
                        case "PERSTAY" -> 1;
                        case "PERGUEST" -> guests;

                        default -> throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Unsupported add-on pricing type: " + pricingType);
                };
        }

        @Override
        @Transactional(readOnly = true)
        public List<BookingResponse> getMyBookings(String customerPhone) {
                return bookingRepository
                                .findByCustomer_PhoneOrderByCreatedAtDesc(customerPhone)
                                .stream()
                                .map(BookingResponse::new)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public BookingResponse getBookingByReference(
                        String bookingReference,
                        String customerPhone) {

                Booking booking = bookingRepository
                                .findByBookingReference(bookingReference)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Booking not found"));

                if (!booking.getCustomer().getPhone().equals(customerPhone)) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "You cannot access this booking");
                }

                return new BookingResponse(booking);
        }

        private void validateRequest(CreateBookingRequest request) {

                LocalDate today = LocalDate.now();

                if (request.getCheckIn() == null || request.getCheckOut() == null) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Check-in and check-out dates are required");
                }

                if (!request.getCheckIn().isAfter(today)) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Check-in must be a future date");
                }

                if (!request.getCheckOut().isAfter(request.getCheckIn())) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Check-out must be after check-in");
                }

                if (request.getGuests() == null || request.getGuests() < 1) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "At least one guest is required");
                }

                long nights = ChronoUnit.DAYS.between(
                                request.getCheckIn(), request.getCheckOut());

                if (nights > 30) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "A booking cannot exceed 30 nights");
                }
        }

        private String normalizePaymentOption(String option) {

                String value = option == null
                                ? "FULL"
                                : option.trim().toUpperCase();

                if (!value.equals("FULL") && !value.equals("ADVANCE")) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Payment option must be FULL or ADVANCE");
                }

                return value;
        }

        private String generateBookingReference() {
                return "VDK-" + UUID.randomUUID()
                                .toString()
                                .replace("-", "")
                                .substring(0, 12)
                                .toUpperCase();
        }
}