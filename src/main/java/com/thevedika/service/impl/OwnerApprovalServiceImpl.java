package com.thevedika.service.impl;

import com.thevedika.dto.admin.OwnerStatusResponse;
import com.thevedika.entity.User;
import com.thevedika.repository.UserRepository;
import com.thevedika.service.OwnerApprovalService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class OwnerApprovalServiceImpl implements OwnerApprovalService {

    private final UserRepository userRepository;

    public OwnerApprovalServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerStatusResponse> getAllOwners() {

        return userRepository
                .findByRoleOrderByCreatedAtDesc("OWNER")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerStatusResponse> getPendingOwners() {

        return userRepository
                .findByRoleAndApprovedOrderByCreatedAtDesc(
                        "OWNER",
                        false)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public OwnerStatusResponse approveOwner(Long ownerId) {

        User owner = findOwner(ownerId);

        if (Boolean.TRUE.equals(owner.getApproved())
                && owner.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Owner is already approved.");
        }

        owner.setApproved(true);
        owner.setActive(true);

        owner = userRepository.save(owner);

        return toResponse(owner);
    }

    @Override
    public OwnerStatusResponse rejectOwner(Long ownerId) {

        User owner = findOwner(ownerId);

        owner.setApproved(false);
        owner.setActive(false);

        owner = userRepository.save(owner);

        return toResponse(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerStatusResponse getOwnerStatus(String phone) {

        User owner = userRepository.findByPhone(phone)
                .filter(user -> "OWNER".equals(user.getRole()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner account not found."));

        return toResponse(owner);
    }

    private User findOwner(Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner not found."));

        if (!"OWNER".equals(owner.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The selected user is not an owner.");
        }

        return owner;
    }

    private OwnerStatusResponse toResponse(User user) {

        return new OwnerStatusResponse(
                user.getId(),
                user.getPhone(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getApproved());
    }
}