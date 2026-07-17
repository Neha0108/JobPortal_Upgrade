package com.recruitment.platform.dto.job;

import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.JobType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record JobResponse(
        UUID id,
        String title,
        String description,
        String requirements,
        String location,
        JobType jobType,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        JobStatus status,
        UUID companyId,
        String companyName,
        UUID recruiterProfileId,
        String recruiterName,
        long applicantCount,
        Instant createdAt
) {
}