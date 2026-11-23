
package com.thevedika.controller;

import com.thevedika.dto.customer.CustomerProfileResponse;
import com.thevedika.dto.customer.UpdateCustomerProfileRequest;
import com.thevedika.entity.User;
import com.thevedika.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final UserRepository userRepository;

    public CustomerController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public CustomerProfileResponse getMyProfile(Authentication authentication) {
        User customer = getCurrentCustomer(authentication);
        return toResponse(customer);
    }

    @PutMapping("/me")
    @Transactional
    public CustomerProfileResponse updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateCustomerProfileRequest request) {

        User customer = getCurrentCustomer(authentication);

        customer.setFullName(request.fullName().trim());

        String email = request.email();
        customer.setEmail(
                email == null || email.isBlank() ? null : email.trim());

        return toResponse(userRepository.save(customer));
    }

    @DeleteMapping("/me")
    @Transactional
    public ResponseEntity<Map<String, String>> deactivateMyAccount(
            Authentication authentication) {

        User customer = getCurrentCustomer(authentication);

        customer.setActive(false);
        userRepository.save(customer);

        return ResponseEntity.ok(
                Map.of("message", "Your account has been deactivated successfully"));
    }

    private User getCurrentCustomer(Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

        User customer = userRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer account not found"));

        if (!"CUSTOMER".equalsIgnoreCase(customer.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This endpoint is only available to customers");
        }

        if (!customer.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account is inactive");
        }

        return customer;
    }

    private CustomerProfileResponse toResponse(User customer) {
        return new CustomerProfileResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getRole(),
                customer.isActive(),
                customer.getCreatedAt());
    }
}
