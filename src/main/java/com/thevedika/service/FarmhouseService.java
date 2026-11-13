package com.thevedika.service;

import com.thevedika.entity.Farmhouse;

import java.util.List;

public interface FarmhouseService {

    // Existing public methods
    List<Farmhouse> getAllFarmhouses();

    Farmhouse getFarmhouseById(Long id);

    Farmhouse createFarmhouse(Farmhouse farmhouse);

    // Admin methods
    List<Farmhouse> getAllFarmhousesForAdmin();

    Farmhouse getFarmhouseForAdmin(Long id);

    Farmhouse updateFarmhouse(Long id, Farmhouse farmhouse);

    Farmhouse changeFarmhouseStatus(Long id, boolean active);

    Farmhouse assignOwner(Long farmhouseId, Long ownerId);
}