package com.thevedika.repository;

import com.thevedika.entity.FarmhouseAddon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmhouseAddonRepository
        extends JpaRepository<FarmhouseAddon, Long> {

    List<FarmhouseAddon> findByFarmhouseIdAndAvailableTrue(Long farmhouseId);

}