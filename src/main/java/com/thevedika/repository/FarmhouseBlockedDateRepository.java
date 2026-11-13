
package com.thevedika.repository;

import com.thevedika.entity.FarmhouseBlockedDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FarmhouseBlockedDateRepository
                extends JpaRepository<FarmhouseBlockedDate, Long> {

        List<FarmhouseBlockedDate> findByFarmhouseIdAndBlockedDateBetweenOrderByBlockedDateAsc(
                        Long farmhouseId,
                        LocalDate startDate,
                        LocalDate endDate);

        Optional<FarmhouseBlockedDate> findByFarmhouseIdAndBlockedDate(
                        Long farmhouseId,
                        LocalDate blockedDate);

        Optional<FarmhouseBlockedDate> findByIdAndFarmhouseId(
                        Long id,
                        Long farmhouseId);

        void deleteByFarmhouseIdAndBlockedDate(
                        Long farmhouseId,
                        LocalDate blockedDate);

}
