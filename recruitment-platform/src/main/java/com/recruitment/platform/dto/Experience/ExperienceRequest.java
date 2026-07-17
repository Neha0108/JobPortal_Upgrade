package com.recruitment.platform.dto.Experience;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ExperienceRequest(
        @NotBlank String companyName,
        @NotBlank String jobTitle,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}
