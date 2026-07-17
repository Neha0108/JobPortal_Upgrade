package com.recruitment.platform.dto.candidate;

import java.util.UUID;

public record CandidateProfileResponse(
        UUID id,
        String fullName,
        String phone,
        String headline,
        String summary,
        String email
) {
}
