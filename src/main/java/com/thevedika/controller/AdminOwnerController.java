package com.thevedika.controller;

import com.thevedika.dto.admin.OwnerStatusResponse;
import com.thevedika.service.OwnerApprovalService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/owners")
public class AdminOwnerController {

    private final OwnerApprovalService ownerApprovalService;

    public AdminOwnerController(
            OwnerApprovalService ownerApprovalService) {
        this.ownerApprovalService = ownerApprovalService;
    }

    // View all owners
    @GetMapping
    public ResponseEntity<List<OwnerStatusResponse>> getAllOwners() {

        return ResponseEntity.ok(
                ownerApprovalService.getAllOwners());
    }

    // View owners waiting for approval
    @GetMapping("/pending")
    public ResponseEntity<List<OwnerStatusResponse>> getPendingOwners() {

        return ResponseEntity.ok(
                ownerApprovalService.getPendingOwners());
    }

    // Approve owner
    @PutMapping("/{ownerId}/approve")
    public ResponseEntity<OwnerStatusResponse> approveOwner(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(
                ownerApprovalService.approveOwner(ownerId));
    }

    // Reject owner
    @PutMapping("/{ownerId}/reject")
    public ResponseEntity<Map<String, Object>> rejectOwner(
            @PathVariable Long ownerId) {

        OwnerStatusResponse response = ownerApprovalService.rejectOwner(ownerId);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Owner application rejected successfully.",
                        "owner", response));
    }
}