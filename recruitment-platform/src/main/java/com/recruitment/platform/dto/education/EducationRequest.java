package com.recruitment.platform.dto.education;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record EducationRequest(
        @NotBlank String institution,
        @NotBlank String degree,
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate
) {
}
