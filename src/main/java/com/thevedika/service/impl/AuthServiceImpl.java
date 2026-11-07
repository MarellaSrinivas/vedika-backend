
package com.thevedika.service.impl;

import com.thevedika.dto.auth.AuthResponse;
import com.thevedika.dto.auth.SendOtpRequest;
import com.thevedika.dto.auth.VerifyOtpRequest;
import com.thevedika.dto.auth.SessionResponse;

import com.thevedika.entity.User;
import com.thevedika.entity.UserSession;

import com.thevedika.repository.UserRepository;
import com.thevedika.repository.UserSessionRepository;

import com.thevedika.security.JwtService;
import com.thevedika.security.TokenHashUtil;

import com.thevedika.service.AuthService;
import com.thevedika.service.Fast2SmsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserSessionRepository sessionRepository;
    private final JwtService jwtService;
    private final Fast2SmsService fast2SmsService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${jwt.expiration:86400000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration-days:30}")
    private long refreshTokenExpirationDays;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserSessionRepository sessionRepository,
            JwtService jwtService,
            Fast2SmsService fast2SmsService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtService = jwtService;
        this.fast2SmsService = fast2SmsService;
    }

    @Override
    public String sendOtp(SendOtpRequest request) {

        String phone = request.getPhone();
        String purpose = normalizePurpose(request.getPurpose());

        if ("LOGIN".equals(purpose)
                && !userRepository.existsByPhone(phone)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Account not found. Please register first.");
        }

        if ("REGISTER".equals(purpose)
                && userRepository.existsByPhone(phone)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account already exists for this phone number.");
        }

        fast2SmsService.sendOtp(phone);

        return "OTP sent successfully";
    }

    @Override
    @Transactional
    public AuthResponse verifyOtp(
            VerifyOtpRequest request,
            String deviceInfo,
            String ipAddress) {

        String phone = request.getPhone();
        String purpose = normalizePurpose(request.getPurpose());

        fast2SmsService.verifyOtp(phone, request.getOtp());

        User user = userRepository.findByPhone(phone).orElse(null);

        if ("REGISTER".equals(purpose)) {

            if (user != null) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "An account already exists for this phone number.");
            }

            user = new User();
            user.setPhone(phone);

            String fullName = request.getFullName();

            user.setFullName(
                    fullName == null || fullName.isBlank()
                            ? "Guest"
                            : fullName.trim());

            user.setEmail(
                    request.getEmail() == null
                            || request.getEmail().isBlank()
                                    ? null
                                    : request.getEmail().trim());

            user.setRole("CUSTOMER");
            user.setActive(true);

        } else {

            if (user == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found. Please register first.");
            }
        }

        if (!user.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account is disabled.");
        }

        user = userRepository.save(user);

        return createSession(user, deviceInfo, ipAddress);
    }

    private AuthResponse createSession(
            User user,
            String deviceInfo,
            String ipAddress) {

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
                LocalDateTime.now().plusDays(
                        refreshTokenExpirationDays));

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

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {

        UserSession session = getValidSession(refreshToken);

        User user = session.getUser();

        if (!user.isActive()) {
            session.setRevoked(true);
            sessionRepository.save(session);

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account is disabled.");
        }

        // Rotate refresh token.
        String newRefreshToken = generateRefreshToken();

        session.setRefreshTokenHash(
                TokenHashUtil.hash(newRefreshToken));

        session.setLastUsedAt(LocalDateTime.now());

        session.setExpiresAt(
                LocalDateTime.now().plusDays(
                        refreshTokenExpirationDays));

        sessionRepository.save(session);

        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                accessTokenExpiration / 1000,
                session.getId(),
                user);
    }

    private UserSession getValidSession(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Refresh token is required.");
        }

        String hash = TokenHashUtil.hash(refreshToken);

        UserSession session = sessionRepository
                .findByRefreshTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid refresh token."));

        if (session.isRevoked()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "This session has been logged out.");
        }

        if (!session.getExpiresAt().isAfter(LocalDateTime.now())) {
            session.setRevoked(true);
            sessionRepository.save(session);

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Refresh token expired. Please login again.");
        }

        return session;
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {

        UserSession session = getValidSession(refreshToken);

        session.setRevoked(true);

        sessionRepository.save(session);
    }

    @Override
    @Transactional
    public void logoutAll(String refreshToken) {

        UserSession currentSession = getValidSession(refreshToken);

        sessionRepository.revokeAllByUserId(
                currentSession.getUser().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getSessions(String refreshToken) {

        UserSession currentSession = getValidSession(refreshToken);

        return sessionRepository
                .findByUserIdAndRevokedFalseAndExpiresAtAfter(
                        currentSession.getUser().getId(),
                        LocalDateTime.now())
                .stream()
                .map(SessionResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public void logoutSession(
            String refreshToken,
            Long sessionId) {

        UserSession currentSession = getValidSession(refreshToken);

        UserSession targetSession = sessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Session not found."));

        if (!targetSession.getUser().getId().equals(
                currentSession.getUser().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot revoke another user's session.");
        }

        targetSession.setRevoked(true);
        sessionRepository.save(targetSession);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveSessionCount() {

        return sessionRepository.countByRevokedFalseAndExpiresAtAfter(
                LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveUserCount() {

        return sessionRepository
                .findAll()
                .stream()
                .filter(s -> !s.isRevoked())
                .filter(s -> s.getExpiresAt().isAfter(
                        LocalDateTime.now()))
                .map(s -> s.getUser().getId())
                .distinct()
                .count();
    }

    private String normalizePurpose(String purpose) {

        String normalized = purpose == null
                ? ""
                : purpose.trim().toUpperCase();

        if (!normalized.equals("LOGIN")
                && !normalized.equals("REGISTER")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Purpose must be LOGIN or REGISTER");
        }

        return normalized;
    }
}
