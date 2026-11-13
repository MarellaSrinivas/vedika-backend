package com.thevedika.service;

import com.thevedika.dto.admin.OwnerStatusResponse;

import java.util.List;

public interface OwnerApprovalService {

    List<OwnerStatusResponse> getAllOwners();

    List<OwnerStatusResponse> getPendingOwners();

    OwnerStatusResponse approveOwner(Long ownerId);

    OwnerStatusResponse rejectOwner(Long ownerId);

    OwnerStatusResponse getOwnerStatus(String phone);
}