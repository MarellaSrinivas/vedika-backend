
package com.thevedika.dto.auth;

import com.thevedika.entity.UserSession;
import java.time.LocalDateTime;

public class SessionResponse {

    private Long id;
    private String deviceInfo;
    private String ipAddress;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime expiresAt;

    public SessionResponse(UserSession session) {
        this.id = session.getId();
        this.deviceInfo = session.getDeviceInfo();
        this.ipAddress = session.getIpAddress();
        this.createdAt = session.getCreatedAt();
        this.lastUsedAt = session.getLastUsedAt();
        this.expiresAt = session.getExpiresAt();
    }

    public Long getId() {
        return id;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
