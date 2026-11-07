package com.thevedika.repository;

import com.thevedika.entity.FarmhouseAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmhouseAmenityRepository
        extends JpaRepository<FarmhouseAmenity, Long> {

    List<FarmhouseAmenity> findByFarmhouseId(Long farmhouseId);

}