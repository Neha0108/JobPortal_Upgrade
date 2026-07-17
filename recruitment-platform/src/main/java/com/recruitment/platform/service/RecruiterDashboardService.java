package com.recruitment.platform.service;

import com.recruitment.platform.dto.recruiter.RecruiterDashboardResponse;
import com.recruitment.platform.entity.ApplicationStatus;
import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.RecruiterProfile;
import com.recruitment.platform.repository.JobApplicationRepository;
import com.recruitment.platform.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruiterDashboardService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final RecruiterProfileService recruiterProfileService;

    public RecruiterDashboardResponse getMyDashboard(UUID userId) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);
        UUID recruiterId = profile.getId();

        long total = jobRepository.countByRecruiterProfileIdAndDeletedAtIsNull(recruiterId);
        long open = jobRepository.countByRecruiterProfileIdAndStatusAndDeletedAtIsNull(recruiterId, JobStatus.OPEN);
        long draft = jobRepository.countByRecruiterProfileIdAndStatusAndDeletedAtIsNull(recruiterId, JobStatus.DRAFT);
        long closed = jobRepository.countByRecruiterProfileIdAndStatusAndDeletedAtIsNull(recruiterId, JobStatus.CLOSED);

        long totalApplicants = jobApplicationRepository.countByJob_RecruiterProfile_Id(recruiterId);
        long shortlisted = jobApplicationRepository.countByJob_RecruiterProfile_IdAndStatus(recruiterId, ApplicationStatus.SHORTLISTED);
        long hired = jobApplicationRepository.countByJob_RecruiterProfile_IdAndStatus(recruiterId, ApplicationStatus.HIRED);

        return new RecruiterDashboardResponse(total, open, draft, closed, totalApplicants, shortlisted, hired);
    }
}
