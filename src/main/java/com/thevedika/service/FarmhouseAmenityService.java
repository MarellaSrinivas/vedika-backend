
package com.thevedika.service;

import com.thevedika.dto.AmenityResponse;
import com.thevedika.dto.FarmhouseAmenitiesRequest;
import com.thevedika.entity.Amenity;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAmenity;
import com.thevedika.entity.User;
import com.thevedika.repository.AmenityRepository;
import com.thevedika.repository.FarmhouseAmenityRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FarmhouseAmenityService {

    private final AmenityRepository amenityRepository;
    private final FarmhouseAmenityRepository farmhouseAmenityRepository;
    private final FarmhouseRepository farmhouseRepository;
    private final UserRepository userRepository;

    public FarmhouseAmenityService(
            AmenityRepository amenityRepository,
            FarmhouseAmenityRepository farmhouseAmenityRepository,
            FarmhouseRepository farmhouseRepository,
            UserRepository userRepository) {
        this.amenityRepository = amenityRepository;
        this.farmhouseAmenityRepository = farmhouseAmenityRepository;
        this.farmhouseRepository = farmhouseRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<AmenityResponse> getAllAmenities() {
        return amenityRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AmenityResponse> getAdminFarmhouseAmenities(
            Long farmhouseId) {
        verifyFarmhouseExists(farmhouseId);
        return getFarmhouseAmenities(farmhouseId);
    }

    @Transactional
    public List<AmenityResponse> updateAdminFarmhouseAmenities(
            Long farmhouseId,
            FarmhouseAmenitiesRequest request) {
        Farmhouse farmhouse = verifyFarmhouseExists(farmhouseId);
        return replaceAmenities(farmhouse, request);
    }

    @Transactional(readOnly = true)
    public List<AmenityResponse> getOwnerFarmhouseAmenities(
            String phone,
            Long farmhouseId) {
        verifyOwnerFarmhouse(phone, farmhouseId);
        return getFarmhouseAmenities(farmhouseId);
    }

    @Transactional
    public List<AmenityResponse> updateOwnerFarmhouseAmenities(
            String phone,
            Long farmhouseId,
            FarmhouseAmenitiesRequest request) {
        Farmhouse farmhouse = verifyOwnerFarmhouse(phone, farmhouseId);
        return replaceAmenities(farmhouse, request);
    }

    private List<AmenityResponse> getFarmhouseAmenities(
            Long farmhouseId) {
        return farmhouseAmenityRepository
                .findByFarmhouseId(farmhouseId)
                .stream()
                .map(FarmhouseAmenity::getAmenity)
                .map(this::toResponse)
                .toList();
    }

    private List<AmenityResponse> replaceAmenities(
            Farmhouse farmhouse,
            FarmhouseAmenitiesRequest request) {
        if (request == null || request.getAmenityIds() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "amenityIds is required. Send an empty list to remove all amenities.");
        }

        List<Long> ids = request.getAmenityIds();

        if (ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amenity IDs must be positive numbers");
        }

        Set<Long> uniqueIds = new HashSet<>(ids);

        if (uniqueIds.size() != ids.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Duplicate amenity IDs are not allowed");
        }

        List<Amenity> amenities = amenityRepository.findAllById(ids);

        if (amenities.size() != uniqueIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more amenity IDs do not exist");
        }

        farmhouseAmenityRepository.deleteByFarmhouseId(
                farmhouse.getId());

        List<FarmhouseAmenity> assignments = new ArrayList<>();

        for (Amenity amenity : amenities) {
            FarmhouseAmenity assignment = new FarmhouseAmenity();
            assignment.setFarmhouse(farmhouse);
            assignment.setAmenity(amenity);
            assignments.add(assignment);
        }

        farmhouseAmenityRepository.saveAll(assignments);

        return amenities.stream()
                .map(this::toResponse)
                .toList();
    }

    private Farmhouse verifyFarmhouseExists(Long farmhouseId) {
        return farmhouseRepository.findById(farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));
    }

    private Farmhouse verifyOwnerFarmhouse(
            String phone,
            Long farmhouseId) {
        User owner = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner account not found"));

        return farmhouseRepository
                .findByIdAndOwner_Id(farmhouseId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found for this owner"));
    }

    private AmenityResponse toResponse(Amenity amenity) {
        return new AmenityResponse(
                amenity.getId(),
                amenity.getName());
    }
}
