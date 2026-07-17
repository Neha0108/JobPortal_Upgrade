package com.recruitment.platform.dto.savedjob;

import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.JobType;

import java.time.Instant;
import java.util.UUID;

public record SavedJobResponse(
        UUID savedJobId,
        UUID jobId,
        String jobTitle,
        String companyName,
        String location,
        JobType jobType,
        JobStatus jobStatus,
        Instant savedAt
) {
}