package com.thevedika.repository;

import com.thevedika.entity.Farmhouse;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface FarmhouseRepository extends JpaRepository<Farmhouse, Long> {

    List<Farmhouse> findByActiveTrue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Farmhouse f WHERE f.id = :id AND f.active = true")
    Optional<Farmhouse> findActiveByIdForUpdate(@Param("id") Long id);
}
