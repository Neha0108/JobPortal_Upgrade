package com.recruitment.platform.dto.auth;

import com.recruitment.platform.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "Password must be at least 8 characters and include an uppercase letter, a lowercase letter, a digit, and a special character"
        )
        String password,

        // Only ADMIN accounts are excluded here - self-service registration
        // is limited to CANDIDATE and RECRUITER. Admins are provisioned separately.
        @NotNull(message = "Role is required")
        Role role
) {
}
