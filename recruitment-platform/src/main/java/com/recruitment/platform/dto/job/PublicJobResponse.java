package com.recruitment.platform.dto.job;

import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.JobType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** Deliberately excludes recruiterProfileId and applicantCount - both are
 *  internal recruiter-facing details, not something an anonymous browser
 *  or candidate needs to see on a public job listing. */
public record PublicJobResponse(
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
        Instant createdAt
) {
}