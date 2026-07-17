package com.recruitment.platform.dto.job;

import com.recruitment.platform.entity.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** Used for both create and update - PATCH-like partial updates aren't
 *  needed here since the recruiter's edit form naturally submits the full job. */
public record JobRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        String requirements,

        String location,

        @NotNull(message = "Job type is required")
        JobType jobType,

        BigDecimal minSalary,

        BigDecimal maxSalary
) {
}