package com.recruitment.platform.dto.candidate;

public record CandidateDashboardResponse(
        long totalApplications,
        long shortlistedCount,
        long interviewCount,
        long hiredCount,
        long savedJobsCount,
        long resumeCount
) {
}