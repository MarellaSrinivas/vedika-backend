package com.thevedika.service.impl;

import com.thevedika.dto.admin.AdminCustomerResponse;
import com.thevedika.entity.User;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.AdminCustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminCustomerServiceImpl implements AdminCustomerService {

    private final UserRepository userRepository;

    public AdminCustomerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AdminCustomerResponse activateCustomer(Long customerId) {
        User customer = getCustomer(customerId);

        customer.setActive(true);

        return toResponse(userRepository.save(customer));
    }

    @Override
    @Transactional
    public AdminCustomerResponse deactivateCustomer(Long customerId) {
        User customer = getCustomer(customerId);

        customer.setActive(false);

        return toResponse(userRepository.save(customer));
    }

    private User getCustomer(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"));

        if (!"CUSTOMER".equalsIgnoreCase(customer.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This account is not a customer");
        }

        return customer;
    }

    private AdminCustomerResponse toResponse(User customer) {
        return new AdminCustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getRole(),
                customer.isActive(),
                customer.getCreatedAt());
    }
}