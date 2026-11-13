
package com.thevedika.repository;

import com.thevedika.entity.FarmhouseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FarmhouseImageRepository
        extends JpaRepository<FarmhouseImage, Long> {

    List<FarmhouseImage> findByFarmhouseIdOrderByDisplayOrderAsc(
            Long farmhouseId);

    Optional<FarmhouseImage> findByIdAndFarmhouseId(
            Long id,
            Long farmhouseId);

    @Modifying
    @Query("""
                UPDATE FarmhouseImage image
                SET image.coverImage = false
                WHERE image.farmhouse.id = :farmhouseId
            """)
    void clearCoverImage(@Param("farmhouseId") Long farmhouseId);

    void deleteByIdAndFarmhouseId(Long id, Long farmhouseId);
}
