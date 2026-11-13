
package com.thevedika.dto.admin;

import java.time.LocalDateTime;

public record AdminCustomerResponse(
        Long id,
        String fullName,
        String phone,
        String email,
        String role,
        boolean active,
        LocalDateTime createdAt) {
}
