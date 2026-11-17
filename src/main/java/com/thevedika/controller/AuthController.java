
package com.thevedika.controller;

import com.thevedika.dto.auth.AuthResponse;
import com.thevedika.dto.auth.SendOtpRequest;
import com.thevedika.dto.auth.VerifyOtpRequest;
import com.thevedika.dto.auth.RefreshTokenRequest;
import com.thevedika.dto.auth.SessionResponse;
import com.thevedika.service.ReviewerAuthService;
import com.thevedika.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthService authService;
        private final ReviewerAuthService reviewerAuthService;

        public AuthController(AuthService authService, ReviewerAuthService reviewerAuthService) {
                this.authService = authService;
                this.reviewerAuthService = reviewerAuthService;
        }

        @PostMapping("/otp/send")
        public ResponseEntity<Map<String, String>> sendOtp(
                        @Valid @RequestBody SendOtpRequest request) {

                String message = authService.sendOtp(request);

                return ResponseEntity.ok(Map.of("message", message));
        }

        @PostMapping("/otp/verify")
        public ResponseEntity<AuthResponse> verifyOtp(
                        @Valid @RequestBody VerifyOtpRequest request,
                        HttpServletRequest httpRequest) {

                String deviceInfo = httpRequest.getHeader("User-Agent");

                String ipAddress = httpRequest.getRemoteAddr();

                return ResponseEntity.ok(
                                authService.verifyOtp(
                                                request,
                                                deviceInfo,
                                                ipAddress));
        }

        @PostMapping("/reviewer/login")
        public ResponseEntity<AuthResponse> reviewerLogin(
                        @RequestBody Map<String, String> request,
                        HttpServletRequest httpRequest) {

                String deviceInfo = httpRequest.getHeader("User-Agent");
                String ipAddress = httpRequest.getRemoteAddr();

                AuthResponse response = reviewerAuthService.login(
                                request.get("phone"),
                                request.get("otp"),
                                deviceInfo,
                                ipAddress);

                return ResponseEntity.ok(response);
        }

        // Refresh access token and rotate refresh token.
        @PostMapping("/token/refresh")
        public ResponseEntity<AuthResponse> refreshToken(
                        @Valid @RequestBody RefreshTokenRequest request) {

                return ResponseEntity.ok(
                                authService.refreshToken(
                                                request.getRefreshToken()));
        }

        // Logout current device.
        @PostMapping("/logout")
        public ResponseEntity<Map<String, String>> logout(
                        @Valid @RequestBody RefreshTokenRequest request) {

                authService.logout(request.getRefreshToken());

                return ResponseEntity.ok(
                                Map.of("message", "Logged out successfully"));
        }

        // Logout all devices.
        @PostMapping("/logout-all")
        public ResponseEntity<Map<String, String>> logoutAll(
                        @Valid @RequestBody RefreshTokenRequest request) {

                authService.logoutAll(request.getRefreshToken());

                return ResponseEntity.ok(
                                Map.of("message", "Logged out from all devices"));
        }

        // List active devices belonging to this customer.
        @PostMapping("/sessions")
        public ResponseEntity<List<SessionResponse>> getSessions(
                        @Valid @RequestBody RefreshTokenRequest request) {

                return ResponseEntity.ok(
                                authService.getSessions(
                                                request.getRefreshToken()));
        }

        // Logout a particular device.
        @DeleteMapping("/sessions/{sessionId}")
        public ResponseEntity<Map<String, String>> logoutSession(
                        @PathVariable Long sessionId,
                        @Valid @RequestBody RefreshTokenRequest request) {

                authService.logoutSession(
                                request.getRefreshToken(),
                                sessionId);

                return ResponseEntity.ok(
                                Map.of("message", "Device logged out successfully"));
        }
}
