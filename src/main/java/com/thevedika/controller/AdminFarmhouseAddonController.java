
package com.thevedika.controller;

import com.thevedika.dto.FarmhouseAddonRequest;
import com.thevedika.dto.FarmhouseAddonResponse;
import com.thevedika.service.FarmhouseAddonManagementService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/farmhouses/{farmhouseId}/addons")
public class AdminFarmhouseAddonController {

    private final FarmhouseAddonManagementService addonService;

    public AdminFarmhouseAddonController(
            FarmhouseAddonManagementService addonService) {
        this.addonService = addonService;
    }

    @GetMapping
    public List<FarmhouseAddonResponse> getAddons(
            @PathVariable Long farmhouseId) {
        return addonService.getAdminAddons(farmhouseId);
    }

    @PostMapping
    public FarmhouseAddonResponse addAddon(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseAddonRequest request) {
        return addonService.addAdminAddon(farmhouseId, request);
    }

    @PutMapping("/{addonId}")
    public FarmhouseAddonResponse updateAddon(
            @PathVariable Long farmhouseId,
            @PathVariable Long addonId,
            @RequestBody FarmhouseAddonRequest request) {
        return addonService.updateAdminAddon(
                farmhouseId, addonId, request);
    }

    @DeleteMapping("/{addonId}")
    public void deleteAddon(
            @PathVariable Long farmhouseId,
            @PathVariable Long addonId) {
        addonService.deleteAdminAddon(farmhouseId, addonId);
    }
}
