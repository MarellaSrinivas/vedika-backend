
package com.thevedika.repository;

import com.thevedika.entity.FarmhouseAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FarmhouseAmenityRepository
        extends JpaRepository<FarmhouseAmenity, Long> {

    List<FarmhouseAmenity> findByFarmhouseId(Long farmhouseId);

    @Modifying
    @Query("""
                DELETE FROM FarmhouseAmenity fa
                WHERE fa.farmhouse.id = :farmhouseId
            """)
    void deleteByFarmhouseId(@Param("farmhouseId") Long farmhouseId);
}
