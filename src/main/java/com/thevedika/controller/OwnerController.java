package com.thevedika.controller;

import com.thevedika.dto.admin.OwnerStatusResponse;
import com.thevedika.service.OwnerApprovalService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerApprovalService ownerApprovalService;

    public OwnerController(
            OwnerApprovalService ownerApprovalService) {
        this.ownerApprovalService = ownerApprovalService;
    }

    @GetMapping("/status")
    public ResponseEntity<OwnerStatusResponse> getOwnerStatus(
            Authentication authentication) {

        String phone = authentication.getName();

        return ResponseEntity.ok(
                ownerApprovalService.getOwnerStatus(phone));
    }
}