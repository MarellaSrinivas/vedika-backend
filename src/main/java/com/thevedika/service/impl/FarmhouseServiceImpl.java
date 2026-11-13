package com.thevedika.service.impl;

import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.User;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.FarmhouseService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class FarmhouseServiceImpl implements FarmhouseService {

        private final FarmhouseRepository farmhouseRepository;
        private final UserRepository userRepository;

        public FarmhouseServiceImpl(
                        FarmhouseRepository farmhouseRepository,
                        UserRepository userRepository) {

                this.farmhouseRepository = farmhouseRepository;
                this.userRepository = userRepository;
        }

        @Override
        @Transactional(readOnly = true)
        public List<Farmhouse> getAllFarmhouses() {
                return farmhouseRepository.findByActiveTrue();
        }

        @Override
        @Transactional(readOnly = true)
        public Farmhouse getFarmhouseById(Long id) {
                return farmhouseRepository.findById(id)
                                .filter(f -> Boolean.TRUE.equals(f.getActive()))
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Farmhouse not found"));
        }

        @Override
        public Farmhouse createFarmhouse(Farmhouse farmhouse) {

                // Owner assignment is handled through a separate,
                // validated admin endpoint.
                farmhouse.setOwner(null);

                return farmhouseRepository.save(farmhouse);
        }

        @Override
        @Transactional(readOnly = true)
        public List<Farmhouse> getAllFarmhousesForAdmin() {
                return farmhouseRepository.findAllByOrderByIdDesc();
        }

        @Override
        @Transactional(readOnly = true)
        public Farmhouse getFarmhouseForAdmin(Long id) {

                return farmhouseRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Farmhouse not found"));
        }

        @Override
        public Farmhouse changeFarmhouseStatus(
                        Long id,
                        boolean active) {

                Farmhouse farmhouse = getFarmhouseForAdmin(id);

                farmhouse.setActive(active);

                return farmhouseRepository.save(farmhouse);
        }

        @Override
        public Farmhouse assignOwner(
                        Long farmhouseId,
                        Long ownerId) {

                Farmhouse farmhouse = getFarmhouseForAdmin(farmhouseId);

                User owner = userRepository.findById(ownerId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Owner not found"));

                if (!"OWNER".equals(owner.getRole())
                                || !owner.isApproved()
                                || !owner.isActive()) {

                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Only active and approved owners can be assigned");
                }

                farmhouse.setOwner(owner);

                return farmhouseRepository.save(farmhouse);
        }

        @Override
        public Farmhouse updateFarmhouse(
                        Long id,
                        Farmhouse updatedFarmhouse) {

                Farmhouse existing = getFarmhouseForAdmin(id);

                if (updatedFarmhouse.getName() == null
                                || updatedFarmhouse.getName().isBlank()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Farmhouse name is required");
                }

                if (updatedFarmhouse.getLocation() == null
                                || updatedFarmhouse.getLocation().isBlank()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Farmhouse location is required");
                }

                if (updatedFarmhouse.getBasePrice() == null
                                || updatedFarmhouse.getWeekendPrice() == null
                                || updatedFarmhouse.getExtraGuestPrice() == null
                                || updatedFarmhouse.getBasePrice().signum() < 0
                                || updatedFarmhouse.getWeekendPrice().signum() < 0
                                || updatedFarmhouse.getExtraGuestPrice().signum() < 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Farmhouse prices must be provided and cannot be negative");
                }

                if (updatedFarmhouse.getMinimumGuests() == null
                                || updatedFarmhouse.getMaximumGuests() == null
                                || updatedFarmhouse.getMinimumGuests() < 1
                                || updatedFarmhouse.getMaximumGuests() < updatedFarmhouse.getMinimumGuests()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid guest limits");
                }

                if (updatedFarmhouse.getBedrooms() == null
                                || updatedFarmhouse.getBedrooms() < 0
                                || updatedFarmhouse.getBathrooms() == null
                                || updatedFarmhouse.getBathrooms() < 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid bedroom or bathroom count");
                }

                if (updatedFarmhouse.getCheckInTime() == null
                                || updatedFarmhouse.getCheckInTime().isBlank()
                                || updatedFarmhouse.getCheckOutTime() == null
                                || updatedFarmhouse.getCheckOutTime().isBlank()) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Check-in and checkout times are required");
                }

                existing.setName(updatedFarmhouse.getName().trim());
                existing.setLocation(updatedFarmhouse.getLocation().trim());
                existing.setDescription(updatedFarmhouse.getDescription());

                existing.setPropertyExperience(
                                updatedFarmhouse.getPropertyExperience());

                existing.setAddressLine(updatedFarmhouse.getAddressLine());
                existing.setCity(updatedFarmhouse.getCity());
                existing.setState(updatedFarmhouse.getState());
                existing.setPincode(updatedFarmhouse.getPincode());

                existing.setLatitude(updatedFarmhouse.getLatitude());
                existing.setLongitude(updatedFarmhouse.getLongitude());

                existing.setHouseRules(updatedFarmhouse.getHouseRules());
                existing.setEventsPolicy(updatedFarmhouse.getEventsPolicy());
                existing.setCookingFoodPolicy(
                                updatedFarmhouse.getCookingFoodPolicy());

                existing.setBasePrice(updatedFarmhouse.getBasePrice());
                existing.setWeekendPrice(updatedFarmhouse.getWeekendPrice());
                existing.setExtraGuestPrice(
                                updatedFarmhouse.getExtraGuestPrice());

                existing.setMinimumGuests(updatedFarmhouse.getMinimumGuests());
                existing.setMaximumGuests(updatedFarmhouse.getMaximumGuests());

                existing.setBedrooms(updatedFarmhouse.getBedrooms());
                existing.setBathrooms(updatedFarmhouse.getBathrooms());

                existing.setCheckInTime(updatedFarmhouse.getCheckInTime());
                existing.setCheckOutTime(updatedFarmhouse.getCheckOutTime());

                return farmhouseRepository.save(existing);
        }
}