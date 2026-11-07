package com.thevedika.service.impl;

import com.thevedika.entity.Farmhouse;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.service.FarmhouseService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class FarmhouseServiceImpl implements FarmhouseService {

    private final FarmhouseRepository farmhouseRepository;

    public FarmhouseServiceImpl(
            FarmhouseRepository farmhouseRepository) {
        this.farmhouseRepository = farmhouseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Farmhouse> getAllFarmhouses() {
        return farmhouseRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Farmhouse getFarmhouseById(Long id) {
        return farmhouseRepository.findById(id)
                .filter(farmhouse -> Boolean.TRUE.equals(farmhouse.getActive()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));
    }

    @Override
    public Farmhouse createFarmhouse(Farmhouse farmhouse) {
        return farmhouseRepository.save(farmhouse);
    }
}