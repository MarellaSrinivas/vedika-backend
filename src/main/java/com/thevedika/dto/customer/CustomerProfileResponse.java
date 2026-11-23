
package com.thevedika.dto.customer;

import java.time.LocalDateTime;

public record CustomerProfileResponse(
        Long id,
        String fullName,
        String phone,
        String email,
        String role,
        boolean active,
        LocalDateTime createdAt) {
}
