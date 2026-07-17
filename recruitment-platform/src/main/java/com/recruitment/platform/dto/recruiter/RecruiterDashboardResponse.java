package com.recruitment.platform.dto.recruiter;

public record RecruiterDashboardResponse(
        long totalJobsPosted,
        long openJobs,
        long draftJobs,
        long closedJobs,
        long totalApplicants,
        long shortlistedCount,
        long hiredCount
) {
}
