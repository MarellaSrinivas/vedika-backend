
package com.thevedika.dto.auth;

import com.thevedika.entity.User;

public class AuthResponse {

    private final String token;
    private final String refreshToken;
    private final String tokenType = "Bearer";
    private final long expiresIn;
    private final Long sessionId;
    private final UserResponse user;

    public AuthResponse(
            String token,
            String refreshToken,
            long expiresIn,
            Long sessionId,
            User user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.sessionId = sessionId;
        this.user = new UserResponse(user);
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public UserResponse getUser() {
        return user;
    }

    public static class UserResponse {

        private final Long id;
        private final String phone;
        private final String fullName;
        private final String email;
        private final String role;

        public UserResponse(User user) {
            this.id = user.getId();
            this.phone = user.getPhone();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.role = user.getRole();
        }

        public Long getId() {
            return id;
        }

        public String getPhone() {
            return phone;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }
}
