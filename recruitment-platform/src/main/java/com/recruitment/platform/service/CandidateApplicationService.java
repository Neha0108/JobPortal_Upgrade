package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.DuplicateResourceException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.application.CandidateApplicationMapper;
import com.recruitment.platform.dto.application.CandidateApplicationResponse;
import com.recruitment.platform.entity.*;
import com.recruitment.platform.repository.JobApplicationRepository;
import com.recruitment.platform.repository.JobRepository;
import com.recruitment.platform.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final CandidateProfileService candidateProfileService;
    private final CandidateApplicationMapper applicationMapper;

    public CandidateApplicationResponse applyToJob(UUID userId, UUID jobId, UUID requestedResumeId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);

        Job job = jobRepository.findByIdAndDeletedAtIsNull(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found."));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new BadRequestException("This job is not currently accepting applications.");
        }

        if (jobApplicationRepository.existsByJobIdAndCandidateProfileId(jobId, profile.getId())) {
            throw new DuplicateResourceException("You have already applied to this job.");
        }

        Resume resume = resolveResume(profile, requestedResumeId);

        JobApplication application = JobApplication.builder()
                .job(job)
                .candidateProfile(profile)
                .resume(resume)
                .status(ApplicationStatus.APPLIED)
                .build();

        return applicationMapper.toResponse(jobApplicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public Page<CandidateApplicationResponse> getMyApplications(UUID userId, Pageable pageable) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return jobApplicationRepository.findByCandidateProfileId(profile.getId(), pageable)
                .map(applicationMapper::toResponse);
    }

    private Resume resolveResume(CandidateProfile profile, UUID requestedResumeId) {
        if (requestedResumeId != null) {
            Resume resume = resumeRepository.findById(requestedResumeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resume not found."));
            if (!resume.getCandidateProfile().getId().equals(profile.getId())) {
                throw new AccessDeniedException("You do not have permission to use this resume.");
            }
            return resume;
        }
        return resumeRepository.findByCandidateProfileIdAndPrimaryTrue(profile.getId())
                .orElseThrow(() -> new BadRequestException(
                        "Please upload a resume (or specify one) before applying to a job."));
    }
}