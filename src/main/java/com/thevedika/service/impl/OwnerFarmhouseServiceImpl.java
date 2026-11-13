package com.thevedika.service.impl;

import com.thevedika.dto.farmhouse.FarmhouseRequest;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.User;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.OwnerFarmhouseService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OwnerFarmhouseServiceImpl implements OwnerFarmhouseService {

    private final FarmhouseRepository farmhouseRepository;
    private final UserRepository userRepository;

    public OwnerFarmhouseServiceImpl(
            FarmhouseRepository farmhouseRepository,
            UserRepository userRepository) {
        this.farmhouseRepository = farmhouseRepository;
        this.userRepository = userRepository;
    }

    private User getApprovedOwner(String phone) {

        User owner = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner account not found"));

        if (!"OWNER".equals(owner.getRole())
                || !owner.isActive()
                || !owner.isApproved()) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your owner account is not active or approved");
        }

        return owner;
    }

    private void validate(FarmhouseRequest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Farmhouse name is required");
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Location is required");
        }

        validatePrice(request.getBasePrice(), "Base price");
        validatePrice(request.getWeekendPrice(), "Weekend price");
        validatePrice(request.getExtraGuestPrice(), "Extra guest price");

        if (request.getMinimumGuests() == null
                || request.getMaximumGuests() == null
                || request.getMinimumGuests() < 1
                || request.getMaximumGuests() < request.getMinimumGuests()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid minimum or maximum guest count");
        }

        if (request.getBedrooms() == null || request.getBedrooms() < 0
                || request.getBathrooms() == null || request.getBathrooms() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Bedrooms and bathrooms must be zero or greater");
        }

        if (request.getCheckInTime() == null
                || request.getCheckInTime().isBlank()
                || request.getCheckOutTime() == null
                || request.getCheckOutTime().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Check-in and check-out times are required");
        }
    }

    private void validatePrice(BigDecimal price, String fieldName) {

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    fieldName + " must be zero or greater");
        }
    }

    private void copyRequest(
            FarmhouseRequest request,
            Farmhouse farmhouse) {
        farmhouse.setName(request.getName().trim());
        farmhouse.setLocation(request.getLocation().trim());
        farmhouse.setDescription(request.getDescription());

        farmhouse.setBasePrice(request.getBasePrice());
        farmhouse.setWeekendPrice(request.getWeekendPrice());
        farmhouse.setExtraGuestPrice(request.getExtraGuestPrice());

        farmhouse.setMinimumGuests(request.getMinimumGuests());
        farmhouse.setMaximumGuests(request.getMaximumGuests());

        farmhouse.setBedrooms(request.getBedrooms());
        farmhouse.setBathrooms(request.getBathrooms());

        farmhouse.setCheckInTime(request.getCheckInTime());
        farmhouse.setCheckOutTime(request.getCheckOutTime());

        farmhouse.setPropertyExperience(
                request.getPropertyExperience() != null
                        && !request.getPropertyExperience().isBlank()
                                ? request.getPropertyExperience().trim()
                                : "PRIVATE_STAY");

        farmhouse.setAddressLine(request.getAddressLine());
        farmhouse.setCity(request.getCity());
        farmhouse.setState(request.getState());
        farmhouse.setPincode(request.getPincode());

        farmhouse.setLatitude(request.getLatitude());
        farmhouse.setLongitude(request.getLongitude());

        farmhouse.setHouseRules(request.getHouseRules());
        farmhouse.setEventsPolicy(request.getEventsPolicy());
        farmhouse.setCookingFoodPolicy(request.getCookingFoodPolicy());
    }

    @Override
    public Farmhouse createFarmhouse(
            String phone,
            FarmhouseRequest request) {
        User owner = getApprovedOwner(phone);
        validate(request);

        Farmhouse farmhouse = new Farmhouse();

        copyRequest(request, farmhouse);

        farmhouse.setOwner(owner);
        farmhouse.setRating(BigDecimal.ZERO);
        farmhouse.setReviewCount(0);
        farmhouse.setActive(true);

        return farmhouseRepository.save(farmhouse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Farmhouse> getMyFarmhouses(String phone) {

        User owner = getApprovedOwner(phone);

        return farmhouseRepository.findByOwner_IdOrderByIdDesc(
                owner.getId());
    }

    @Override
    public Farmhouse updateFarmhouse(
            String phone,
            Long farmhouseId,
            FarmhouseRequest request) {
        User owner = getApprovedOwner(phone);
        validate(request);

        Farmhouse farmhouse = farmhouseRepository
                .findByIdAndOwner_Id(farmhouseId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found in your account"));

        copyRequest(request, farmhouse);

        // Preserve owner, rating, review count and active status.
        return farmhouseRepository.save(farmhouse);
    }

    @Override
    public Farmhouse changeFarmhouseStatus(
            String phone,
            Long farmhouseId,
            boolean active) {
        User owner = getApprovedOwner(phone);

        Farmhouse farmhouse = farmhouseRepository
                .findByIdAndOwner_Id(farmhouseId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found in your account"));

        farmhouse.setActive(active);

        return farmhouseRepository.save(farmhouse);
    }
}