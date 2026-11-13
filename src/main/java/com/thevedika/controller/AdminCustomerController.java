
package com.thevedika.controller;

import com.thevedika.dto.admin.AdminCustomerResponse;
import com.thevedika.entity.User;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.AdminCustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/customers")
@Transactional(readOnly = true)
public class AdminCustomerController {

    private final UserRepository userRepository;
    private final AdminCustomerService adminCustomerService;

    public AdminCustomerController(
            UserRepository userRepository,
            AdminCustomerService adminCustomerService) {
        this.userRepository = userRepository;
        this.adminCustomerService = adminCustomerService;
    }

    @GetMapping
    public List<AdminCustomerResponse> getAllCustomers() {

        return userRepository
                .findByRoleOrderByCreatedAtDesc("CUSTOMER")
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AdminCustomerResponse getCustomerById(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .filter(u -> "CUSTOMER".equalsIgnoreCase(u.getRole()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"));

        return convertToResponse(user);
    }

    private AdminCustomerResponse convertToResponse(User user) {

        return new AdminCustomerResponse(
                user.getId(),
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt());
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<AdminCustomerResponse> activateCustomer(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                adminCustomerService.activateCustomer(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AdminCustomerResponse> deactivateCustomer(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                adminCustomerService.deactivateCustomer(id));
    }
}
