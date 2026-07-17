package com.recruitment.platform.dto.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCandidateProfileRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 150)
        String fullName,

        @Size(max = 20)
        String phone,

        @Size(max = 200)
        String headline,

        String summary
) {
}
