package com.thevedika.service;

import com.thevedika.dto.farmhouse.FarmhouseRequest;
import com.thevedika.entity.Farmhouse;

import java.util.List;

public interface OwnerFarmhouseService {

    Farmhouse createFarmhouse(String phone, FarmhouseRequest request);

    List<Farmhouse> getMyFarmhouses(String phone);

    Farmhouse updateFarmhouse(
            String phone,
            Long farmhouseId,
            FarmhouseRequest request);

    Farmhouse changeFarmhouseStatus(
            String phone,
            Long farmhouseId,
            boolean active);
}