package com.recruitment.platform.dto.recruiter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 200)
        String name,

        String description,

        @Size(max = 255)
        String website,

        @Size(max = 100)
        String industry,

        @Size(max = 500)
        String logoUrl
) {
}