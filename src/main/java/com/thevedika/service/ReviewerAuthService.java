package com.thevedika.service;

import com.thevedika.dto.auth.AuthResponse;
import com.thevedika.entity.User;
import com.thevedika.entity.UserSession;
import com.thevedika.repository.UserRepository;
import com.thevedika.repository.UserSessionRepository;
import com.thevedika.security.JwtService;
import com.thevedika.security.TokenHashUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class ReviewerAuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final JwtService jwtService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.review-login.enabled:false}")
    private boolean enabled;

    @Value("${app.review-login.phone:}")
    private String reviewerPhone;

    @Value("${app.review-login.otp:}")
    private String reviewerOtp;

    @Value("${jwt.expiration:86400000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration-days:30}")
    private long refreshTokenExpirationDays;

    public ReviewerAuthService(
            UserRepository userRepository,
            UserSessionRepository sessionRepository,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse login(
            String phone,
            String otp,
            String deviceInfo,
            String ipAddress) {

        if (!enabled) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Reviewer login is disabled.");
        }

        if (phone == null || otp == null
                || !constantTimeEquals(phone, reviewerPhone)
                || !constantTimeEquals(otp, reviewerOtp)) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid reviewer login details.");
        }

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reviewer account not found."));

        if (!user.isActive()
                || !"CUSTOMER".equalsIgnoreCase(user.getRole())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Reviewer account is not available.");
        }

        // Generate normal application tokens.
        String accessToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken();

        UserSession session = new UserSession();

        session.setUser(user);
        session.setRefreshTokenHash(
                TokenHashUtil.hash(refreshToken));
        session.setDeviceInfo(deviceInfo);
        session.setIpAddress(ipAddress);
        session.setLastUsedAt(LocalDateTime.now());
        session.setExpiresAt(
                LocalDateTime.now().plusDays(refreshTokenExpirationDays));

        session = sessionRepository.save(session);

        return new AuthResponse(
                accessToken,
                refreshToken,
                accessTokenExpiration / 1000,
                session.getId(),
                user);
    }

    private String generateRefreshToken() {

        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private boolean constantTimeEquals(String first, String second) {

        if (first == null || second == null) {
            return false;
        }

        return MessageDigest.isEqual(
                first.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                second.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
}