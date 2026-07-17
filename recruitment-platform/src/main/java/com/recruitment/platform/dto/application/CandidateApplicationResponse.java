package com.recruitment.platform.dto.application;

import com.recruitment.platform.entity.ApplicationStatus;

import java.time.Instant;
import java.util.UUID;

public record CandidateApplicationResponse(
        UUID applicationId,
        UUID jobId,
        String jobTitle,
        String companyName,
        UUID resumeId,
        String resumeFileName,
        ApplicationStatus status,
        Instant appliedAt
) {
}
