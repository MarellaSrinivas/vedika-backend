package com.thevedika.service;

import com.thevedika.entity.Farmhouse;

import java.util.List;

public interface FarmhouseService {

    List<Farmhouse> getAllFarmhouses();

    Farmhouse getFarmhouseById(Long id);

    Farmhouse createFarmhouse(Farmhouse farmhouse);
}