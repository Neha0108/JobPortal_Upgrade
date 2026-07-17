package com.recruitment.platform.dto.recruiter;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String name,
        String description,
        String website,
        String industry,
        String logoUrl
) {
}
