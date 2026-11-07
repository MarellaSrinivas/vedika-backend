
package com.thevedika.service;

import com.thevedika.dto.auth.AuthResponse;
import com.thevedika.dto.auth.SendOtpRequest;
import com.thevedika.dto.auth.VerifyOtpRequest;
import com.thevedika.dto.auth.SessionResponse;

import java.util.List;

public interface AuthService {

    String sendOtp(SendOtpRequest request);

    AuthResponse verifyOtp(
            VerifyOtpRequest request,
            String deviceInfo,
            String ipAddress);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    void logoutAll(String refreshToken);

    List<SessionResponse> getSessions(String refreshToken);

    void logoutSession(String refreshToken, Long sessionId);

    long getActiveSessionCount();

    long getActiveUserCount();
}
