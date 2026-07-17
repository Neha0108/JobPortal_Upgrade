package com.recruitment.platform.dto.recruiter;

import java.util.UUID;

public record RecruiterProfileResponse(
        UUID id,
        String fullName,
        String phone,
        String designation,
        String email,
        UUID companyId,
        String companyName
) {
}
