package com.recruitment.platform.dto.education;

import java.time.LocalDate;
import java.util.UUID;

public record EducationResponse(
        UUID id,
        String institution,
        String degree,
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate
) {
}
