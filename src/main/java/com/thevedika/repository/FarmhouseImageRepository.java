package com.thevedika.repository;

import com.thevedika.entity.FarmhouseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmhouseImageRepository extends JpaRepository<FarmhouseImage, Long> {

    List<FarmhouseImage> findByFarmhouseIdOrderByDisplayOrderAsc(Long farmhouseId);

}