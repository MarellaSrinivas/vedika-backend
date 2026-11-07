package com.thevedika.repository;

import com.thevedika.entity.FarmhouseAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FarmhouseAvailabilityRepository
        extends JpaRepository<FarmhouseAvailability, Long> {

    Optional<FarmhouseAvailability> findByFarmhouseIdAndAvailabilityDate(
            Long farmhouseId,
            LocalDate availabilityDate);

    List<FarmhouseAvailability> findByFarmhouseIdAndAvailabilityDateBetween(
            Long farmhouseId,
            LocalDate startDate,
            LocalDate endDate);
}