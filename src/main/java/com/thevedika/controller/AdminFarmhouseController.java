
package com.thevedika.controller;

import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.service.FarmhouseResponseMapper;
import com.thevedika.service.FarmhouseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/farmhouses")
public class AdminFarmhouseController {

        private final FarmhouseService farmhouseService;
        private final FarmhouseResponseMapper responseMapper;

        public AdminFarmhouseController(
                        FarmhouseService farmhouseService,
                        FarmhouseResponseMapper responseMapper) {
                this.farmhouseService = farmhouseService;
                this.responseMapper = responseMapper;
        }

        @GetMapping
        public ResponseEntity<List<FarmhouseResponse>> getAllFarmhouses() {

                List<FarmhouseResponse> response = farmhouseService.getAllFarmhousesForAdmin()
                                .stream()
                                .map(responseMapper::toResponse)
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<FarmhouseResponse> getFarmhouse(
                        @PathVariable Long id) {
                Farmhouse farmhouse = farmhouseService.getFarmhouseForAdmin(id);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }

        @PostMapping
        public ResponseEntity<FarmhouseResponse> createFarmhouse(
                        @RequestBody Farmhouse farmhouse) {
                Farmhouse saved = farmhouseService.createFarmhouse(farmhouse);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(responseMapper.toResponse(saved));
        }

        @PutMapping("/{id}")
        public ResponseEntity<FarmhouseResponse> updateFarmhouse(
                        @PathVariable Long id,
                        @RequestBody Farmhouse farmhouse) {
                Farmhouse updated = farmhouseService.updateFarmhouse(id, farmhouse);

                return ResponseEntity.ok(
                                responseMapper.toResponse(updated));
        }

        @PutMapping("/{id}/activate")
        public ResponseEntity<FarmhouseResponse> activateFarmhouse(
                        @PathVariable Long id) {
                Farmhouse farmhouse = farmhouseService.changeFarmhouseStatus(id, true);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }

        @PutMapping("/{id}/deactivate")
        public ResponseEntity<FarmhouseResponse> deactivateFarmhouse(
                        @PathVariable Long id) {
                Farmhouse farmhouse = farmhouseService.changeFarmhouseStatus(id, false);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }

        @PutMapping("/{id}/assign-owner/{ownerId}")
        public ResponseEntity<FarmhouseResponse> assignOwner(
                        @PathVariable Long id,
                        @PathVariable Long ownerId) {
                Farmhouse farmhouse = farmhouseService.assignOwner(id, ownerId);

                return ResponseEntity.ok(
                                responseMapper.toResponse(farmhouse));
        }
}
