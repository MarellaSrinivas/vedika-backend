package com.thevedika.repository;

import com.thevedika.entity.Farmhouse;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FarmhouseRepository
                extends JpaRepository<Farmhouse, Long> {

        // Public farmhouse listing
        List<Farmhouse> findByActiveTrue();

        // Admin management
        List<Farmhouse> findAllByOrderByIdDesc();

        long countByActiveTrue();

        long countByActiveFalse();

        // Owner management
        List<Farmhouse> findByOwner_IdOrderByIdDesc(Long ownerId);

        List<Farmhouse> findByOwner_IdAndActiveTrueOrderByIdDesc(
                        Long ownerId);

        long countByOwner_Id(Long ownerId);

        Optional<Farmhouse> findByIdAndOwner_Id(Long id, Long ownerId);

        long countByOwner_IdAndActiveTrue(Long ownerId);

        // Existing booking lock query
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT f FROM Farmhouse f
                        WHERE f.id = :id AND f.active = true
                        """)
        Optional<Farmhouse> findActiveByIdForUpdate(
                        @Param("id") Long id);

}