
package com.thevedika.controller;

import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.dto.farmhouse.FarmhouseRequest;
import com.thevedika.entity.Farmhouse;
import com.thevedika.service.FarmhouseResponseMapper;
import com.thevedika.service.OwnerFarmhouseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/farmhouses")
public class OwnerFarmhouseController {

        private final OwnerFarmhouseService ownerFarmhouseService;
        private final FarmhouseResponseMapper responseMapper;

        public OwnerFarmhouseController(
                        OwnerFarmhouseService ownerFarmhouseService,
                        FarmhouseResponseMapper responseMapper) {
                this.ownerFarmhouseService = ownerFarmhouseService;
                this.responseMapper = responseMapper;
        }

        @PostMapping
        public ResponseEntity<FarmhouseResponse> createFarmhouse(
                        @RequestBody FarmhouseRequest request,
                        Authentication authentication) {
                Farmhouse farmhouse = ownerFarmhouseService.createFarmhouse(
                                authentication.getName(),
                                request);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(responseMapper.toResponse(farmhouse));
        }

        @GetMapping
        public ResponseEntity<List<FarmhouseResponse>> getMyFarmhouses(
                        Authentication authentication) {
                List<FarmhouseResponse> response = ownerFarmhouseService
                                .getMyFarmhouses(authentication.getName())
                                .stream()
                                .map(responseMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @PutMapping("/{farmhouseId}")
        public ResponseEntity<FarmhouseResponse> updateFarmhouse(
                        @PathVariable Long farmhouseId,
                        @RequestBody FarmhouseRequest request,
                        Authentication authentication) {
                Farmhouse farmhouse = ownerFarmhouseService.updateFarmhouse(
                                authentication.getName(),
                                farmhouseId,
                                request);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }

        @PutMapping("/{farmhouseId}/activate")
        public ResponseEntity<FarmhouseResponse> activateFarmhouse(
                        @PathVariable Long farmhouseId,
                        Authentication authentication) {
                Farmhouse farmhouse = ownerFarmhouseService.changeFarmhouseStatus(
                                authentication.getName(),
                                farmhouseId,
                                true);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }

        @PutMapping("/{farmhouseId}/deactivate")
        public ResponseEntity<FarmhouseResponse> deactivateFarmhouse(
                        @PathVariable Long farmhouseId,
                        Authentication authentication) {
                Farmhouse farmhouse = ownerFarmhouseService.changeFarmhouseStatus(
                                authentication.getName(),
                                farmhouseId,
                                false);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }
}
