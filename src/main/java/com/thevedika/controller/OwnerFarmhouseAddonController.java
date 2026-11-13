
package com.thevedika.controller;

import com.thevedika.dto.FarmhouseAddonRequest;
import com.thevedika.dto.FarmhouseAddonResponse;
import com.thevedika.service.FarmhouseAddonManagementService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/farmhouses/{farmhouseId}/addons")
public class OwnerFarmhouseAddonController {

    private final FarmhouseAddonManagementService addonService;

    public OwnerFarmhouseAddonController(
            FarmhouseAddonManagementService addonService) {
        this.addonService = addonService;
    }

    @GetMapping
    public List<FarmhouseAddonResponse> getAddons(
            @PathVariable Long farmhouseId,
            Authentication authentication) {
        return addonService.getOwnerAddons(
                authentication.getName(), farmhouseId);
    }

    @PostMapping
    public FarmhouseAddonResponse addAddon(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseAddonRequest request,
            Authentication authentication) {
        return addonService.addOwnerAddon(
                authentication.getName(), farmhouseId, request);
    }

    @PutMapping("/{addonId}")
    public FarmhouseAddonResponse updateAddon(
            @PathVariable Long farmhouseId,
            @PathVariable Long addonId,
            @RequestBody FarmhouseAddonRequest request,
            Authentication authentication) {
        return addonService.updateOwnerAddon(
                authentication.getName(),
                farmhouseId,
                addonId,
                request);
    }

    @DeleteMapping("/{addonId}")
    public void deleteAddon(
            @PathVariable Long farmhouseId,
            @PathVariable Long addonId,
            Authentication authentication) {
        addonService.deleteOwnerAddon(
                authentication.getName(),
                farmhouseId,
                addonId);
    }
}
