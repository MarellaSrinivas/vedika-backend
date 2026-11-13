
package com.thevedika.repository;

import com.thevedika.entity.FarmhouseSpecialPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FarmhouseSpecialPriceRepository
                extends JpaRepository<FarmhouseSpecialPrice, Long> {

        List<FarmhouseSpecialPrice> findByFarmhouseIdAndPriceDateBetweenOrderByPriceDateAsc(
                        Long farmhouseId,
                        LocalDate startDate,
                        LocalDate endDate);

        Optional<FarmhouseSpecialPrice> findByFarmhouseIdAndPriceDate(
                        Long farmhouseId,
                        LocalDate priceDate);

        Optional<FarmhouseSpecialPrice> findByIdAndFarmhouseId(
                        Long id,
                        Long farmhouseId);

        void deleteByFarmhouseIdAndPriceDate(
                        Long farmhouseId,
                        LocalDate priceDate);

}
