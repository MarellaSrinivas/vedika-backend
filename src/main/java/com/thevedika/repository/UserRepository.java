package com.thevedika.repository;

import com.thevedika.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    // Role counts for admin dashboard
    long countByRole(String role);

    // Owner management
    List<User> findByRoleOrderByCreatedAtDesc(String role);

    List<User> findByRoleAndApprovedOrderByCreatedAtDesc(
            String role,
            Boolean approved);

    long countByRoleAndApproved(String role, Boolean approved);

    // Active users
    long countByRoleAndActive(String role, Boolean active);
}