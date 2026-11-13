
package com.thevedika.repository;

import com.thevedika.entity.FarmhouseAddon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmhouseAddonRepository
        extends JpaRepository<FarmhouseAddon, Long> {

    List<FarmhouseAddon> findByFarmhouseId(Long farmhouseId);

    List<FarmhouseAddon> findByFarmhouseIdAndAvailableTrue(Long farmhouseId);

    Optional<FarmhouseAddon> findByIdAndFarmhouseId(
            Long id,
            Long farmhouseId);

    void deleteByIdAndFarmhouseId(Long id, Long farmhouseId);
}
