package com.recruitment.platform.dto.recruiter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRecruiterProfileRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 150)
        String fullName,

        @Size(max = 20)
        String phone,

        @Size(max = 150)
        String designation
) {
}
