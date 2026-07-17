package com.recruitment.platform.dto.Experience;

import java.time.LocalDate;
import java.util.UUID;

public record ExperienceResponse(
        UUID id,
        String companyName,
        String jobTitle,
        String description,
        LocalDate startDate,
        LocalDate endDate
) {
}