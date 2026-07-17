package com.recruitment.platform.dto.application;

import com.recruitment.platform.entity.ApplicationStatus;

import java.time.Instant;
import java.util.UUID;

public record ApplicantResponse(
        UUID applicationId,
        UUID candidateProfileId,
        String candidateName,
        String candidateEmail,
        UUID resumeId,
        String resumeFileName,
        ApplicationStatus status,
        Instant appliedAt
) {
}