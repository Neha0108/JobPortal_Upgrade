package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.application.ApplicantResponse;
import com.recruitment.platform.dto.application.JobApplicationMapper;
import com.recruitment.platform.entity.ApplicationStatus;
import com.recruitment.platform.entity.Job;
import com.recruitment.platform.entity.JobApplication;
import com.recruitment.platform.entity.RecruiterProfile;
import com.recruitment.platform.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruiterApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final RecruiterProfileService recruiterProfileService;
    private final JobApplicationMapper jobApplicationMapper;

    // Recruiters may only move an application into one of these states -
    // APPLIED is the initial candidate-set state, never recruiter-set.
    private static final Set<ApplicationStatus> RECRUITER_SETTABLE_STATUSES =
            Set.of(ApplicationStatus.SHORTLISTED, ApplicationStatus.INTERVIEW,
                    ApplicationStatus.REJECTED, ApplicationStatus.HIRED);

    @Transactional(readOnly = true)
    public Page<ApplicantResponse> getApplicantsForJob(UUID userId, UUID jobId, Pageable pageable) {
        return jobApplicationRepository.findByJobId(jobId, pageable)
                .map(app -> {
                    verifyOwnership(userId, app.getJob());
                    return jobApplicationMapper.toResponse(app);
                });
    }

    public ApplicantResponse shortlistCandidate(UUID userId, UUID applicationId) {
        return updateStatus(userId, applicationId, ApplicationStatus.SHORTLISTED);
    }

    public ApplicantResponse rejectCandidate(UUID userId, UUID applicationId) {
        return updateStatus(userId, applicationId, ApplicationStatus.REJECTED);
    }

    public ApplicantResponse updateStatus(UUID userId, UUID applicationId, ApplicationStatus newStatus) {
        if (!RECRUITER_SETTABLE_STATUSES.contains(newStatus)) {
            throw new BadRequestException("Recruiters cannot set an application to status: " + newStatus);
        }

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found."));

        verifyOwnership(userId, application.getJob());

        application.setStatus(newStatus);
        return jobApplicationMapper.toResponse(jobApplicationRepository.save(application));
    }

    private void verifyOwnership(UUID userId, Job job) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);
        if (!job.getRecruiterProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You do not have permission to view or manage applicants for this job.");
        }
    }
}