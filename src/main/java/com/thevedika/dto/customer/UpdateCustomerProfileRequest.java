
package com.thevedika.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCustomerProfileRequest(

        @NotBlank(message = "Full name is required") @Size(max = 120, message = "Full name cannot exceed 120 characters") String fullName,

        @Email(message = "Please enter a valid email address") @Size(max = 150, message = "Email cannot exceed 150 characters") String email) {
}
