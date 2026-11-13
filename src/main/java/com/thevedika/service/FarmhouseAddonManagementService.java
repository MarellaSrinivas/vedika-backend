
package com.thevedika.service;

import com.thevedika.dto.FarmhouseAddonRequest;
import com.thevedika.dto.FarmhouseAddonResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseAddon;
import com.thevedika.entity.User;
import com.thevedika.repository.FarmhouseAddonRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FarmhouseAddonManagementService {

    private final FarmhouseAddonRepository addonRepository;
    private final FarmhouseRepository farmhouseRepository;
    private final UserRepository userRepository;

    public FarmhouseAddonManagementService(
            FarmhouseAddonRepository addonRepository,
            FarmhouseRepository farmhouseRepository,
            UserRepository userRepository) {
        this.addonRepository = addonRepository;
        this.farmhouseRepository = farmhouseRepository;
        this.userRepository = userRepository;
    }

    // ADMIN

    @Transactional(readOnly = true)
    public List<FarmhouseAddonResponse> getAdminAddons(Long farmhouseId) {
        verifyFarmhouse(farmhouseId);

        return addonRepository.findByFarmhouseId(farmhouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FarmhouseAddonResponse addAdminAddon(
            Long farmhouseId,
            FarmhouseAddonRequest request) {
        Farmhouse farmhouse = verifyFarmhouse(farmhouseId);

        FarmhouseAddon addon = new FarmhouseAddon();
        addon.setFarmhouse(farmhouse);

        applyRequest(addon, request);

        return toResponse(addonRepository.save(addon));
    }

    @Transactional
    public FarmhouseAddonResponse updateAdminAddon(
            Long farmhouseId,
            Long addonId,
            FarmhouseAddonRequest request) {
        FarmhouseAddon addon = getAddon(farmhouseId, addonId);

        applyRequest(addon, request);

        return toResponse(addonRepository.save(addon));
    }

    @Transactional
    public void deleteAdminAddon(Long farmhouseId, Long addonId) {
        getAddon(farmhouseId, addonId);
        addonRepository.deleteByIdAndFarmhouseId(addonId, farmhouseId);
    }

    // OWNER

    @Transactional(readOnly = true)
    public List<FarmhouseAddonResponse> getOwnerAddons(
            String phone,
            Long farmhouseId) {
        verifyOwner(phone, farmhouseId);

        return addonRepository.findByFarmhouseId(farmhouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FarmhouseAddonResponse addOwnerAddon(
            String phone,
            Long farmhouseId,
            FarmhouseAddonRequest request) {
        Farmhouse farmhouse = verifyOwner(phone, farmhouseId);

        FarmhouseAddon addon = new FarmhouseAddon();
        addon.setFarmhouse(farmhouse);

        applyRequest(addon, request);

        return toResponse(addonRepository.save(addon));
    }

    @Transactional
    public FarmhouseAddonResponse updateOwnerAddon(
            String phone,
            Long farmhouseId,
            Long addonId,
            FarmhouseAddonRequest request) {
        verifyOwner(phone, farmhouseId);

        FarmhouseAddon addon = getAddon(farmhouseId, addonId);

        applyRequest(addon, request);

        return toResponse(addonRepository.save(addon));
    }

    @Transactional
    public void deleteOwnerAddon(
            String phone,
            Long farmhouseId,
            Long addonId) {
        verifyOwner(phone, farmhouseId);

        getAddon(farmhouseId, addonId);

        addonRepository.deleteByIdAndFarmhouseId(addonId, farmhouseId);
    }

    // COMMON METHODS

    private Farmhouse verifyFarmhouse(Long farmhouseId) {
        return farmhouseRepository.findById(farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));
    }

    private Farmhouse verifyOwner(String phone, Long farmhouseId) {

        User owner = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found"));

        return farmhouseRepository
                .findByIdAndOwner_Id(farmhouseId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You do not own this farmhouse"));
    }

    private FarmhouseAddon getAddon(Long farmhouseId, Long addonId) {
        return addonRepository
                .findByIdAndFarmhouseId(addonId, farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Add-on not found"));
    }

    private void applyRequest(
            FarmhouseAddon addon,
            FarmhouseAddonRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Add-on details are required");
        }

        if (request.getName() == null ||
                request.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Add-on name is required");
        }

        if (request.getPrice() == null ||
                request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A valid add-on price is required");
        }

        String pricingType = request.getPricingType() == null
                ? ""
                : request.getPricingType().trim().toUpperCase();

        if (!pricingType.equals("PER_STAY") &&
                !pricingType.equals("PER_GUEST")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Pricing type must be PER_STAY or PER_GUEST");
        }

        addon.setName(request.getName().trim());
        addon.setDescription(request.getDescription());
        addon.setPrice(request.getPrice());
        addon.setPricingType(pricingType);
        addon.setAvailable(
                request.getAvailable() == null
                        ? true
                        : request.getAvailable());
    }

    private FarmhouseAddonResponse toResponse(FarmhouseAddon addon) {
        return new FarmhouseAddonResponse(
                addon.getId(),
                addon.getName(),
                addon.getDescription(),
                addon.getPrice(),
                addon.getPricingType(),
                addon.getAvailable());
    }
}
