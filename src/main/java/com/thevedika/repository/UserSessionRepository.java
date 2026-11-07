
package com.thevedika.repository;

import com.thevedika.entity.UserSession;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository
        extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshTokenHash(String hash);

    List<UserSession> findByUserIdAndRevokedFalseAndExpiresAtAfter(
            Long userId,
            LocalDateTime now);

    long countByRevokedFalseAndExpiresAtAfter(
            LocalDateTime now);

    @Modifying
    @Query("""
                UPDATE UserSession s
                SET s.revoked = true
                WHERE s.user.id = :userId
                AND s.revoked = false
            """)
    int revokeAllByUserId(@Param("userId") Long userId);
}
