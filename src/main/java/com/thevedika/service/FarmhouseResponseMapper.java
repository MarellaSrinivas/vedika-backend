
package com.thevedika.service;

import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAddon;
import com.thevedika.entity.FarmhouseAmenity;
import com.thevedika.entity.FarmhouseImage;
import com.thevedika.repository.FarmhouseAddonRepository;
import com.thevedika.repository.FarmhouseAmenityRepository;
import com.thevedika.repository.FarmhouseImageRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FarmhouseResponseMapper {

        private final FarmhouseImageRepository imageRepository;
        private final FarmhouseAmenityRepository amenityRepository;
        private final FarmhouseAddonRepository addonRepository;

        public FarmhouseResponseMapper(
                        FarmhouseImageRepository imageRepository,
                        FarmhouseAmenityRepository amenityRepository,
                        FarmhouseAddonRepository addonRepository) {
                this.imageRepository = imageRepository;
                this.amenityRepository = amenityRepository;
                this.addonRepository = addonRepository;
        }

        @Transactional(readOnly = true)
        public FarmhouseResponse toResponse(Farmhouse farmhouse) {

                FarmhouseResponse response = new FarmhouseResponse();

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
                response.setActive(farmhouse.getActive());

                response.setPropertyExperience(farmhouse.getPropertyExperience());

                response.setAddressLine(farmhouse.getAddressLine());
                response.setCity(farmhouse.getCity());
                response.setState(farmhouse.getState());
                response.setPincode(farmhouse.getPincode());

                response.setLatitude(farmhouse.getLatitude());
                response.setLongitude(farmhouse.getLongitude());

                response.setHouseRules(farmhouse.getHouseRules());
                response.setEventsPolicy(farmhouse.getEventsPolicy());
                response.setCookingFoodPolicy(farmhouse.getCookingFoodPolicy());

                // Map assigned owner
                if (farmhouse.getOwner() != null) {
                        var owner = farmhouse.getOwner();

                        response.setOwner(
                                        new FarmhouseResponse.OwnerResponse(
                                                        owner.getId(),
                                                        owner.getFullName(),
                                                        owner.getPhone(),
                                                        owner.getEmail(),
                                                        owner.isApproved(),
                                                        owner.isActive()));
                } else {
                        response.setOwner(null);
                }

                List<FarmhouseResponse.ImageResponse> images = imageRepository
                                .findByFarmhouseIdOrderByDisplayOrderAsc(farmhouse.getId())
                                .stream()
                                .map(this::mapImage)
                                .toList();

                List<FarmhouseResponse.AmenityResponse> amenities = amenityRepository
                                .findByFarmhouseId(farmhouse.getId())
                                .stream()
                                .map(this::mapAmenity)
                                .toList();

                List<FarmhouseResponse.AddonResponse> addons = addonRepository
                                .findByFarmhouseIdAndAvailableTrue(farmhouse.getId())
                                .stream()
                                .map(this::mapAddon)
                                .toList();

                response.setImages(images);
                response.setAmenities(amenities);
                response.setAddons(addons);

                return response;
        }

        private FarmhouseResponse.ImageResponse mapImage(
                        FarmhouseImage image) {
                return new FarmhouseResponse.ImageResponse(
                                image.getId(),
                                image.getImageUrl(),
                                image.getDisplayOrder(),
                                image.getCoverImage());
        }

        private FarmhouseResponse.AmenityResponse mapAmenity(
                        FarmhouseAmenity farmhouseAmenity) {
                return new FarmhouseResponse.AmenityResponse(
                                farmhouseAmenity.getAmenity().getId(),
                                farmhouseAmenity.getAmenity().getName());
        }

        private FarmhouseResponse.AddonResponse mapAddon(
                        FarmhouseAddon addon) {
                return new FarmhouseResponse.AddonResponse(
                                addon.getId(),
                                addon.getName(),
                                addon.getDescription(),
                                addon.getPrice(),
                                addon.getPricingType().toString(),
                                addon.getAvailable());
        }
}
