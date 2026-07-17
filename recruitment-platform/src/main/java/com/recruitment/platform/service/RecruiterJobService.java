package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.job.JobMapper;
import com.recruitment.platform.dto.job.JobRequest;
import com.recruitment.platform.dto.job.JobResponse;
import com.recruitment.platform.entity.Job;
import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.RecruiterProfile;
import com.recruitment.platform.repository.JobApplicationRepository;
import com.recruitment.platform.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruiterJobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final RecruiterProfileService recruiterProfileService;
    private final JobMapper jobMapper;

    public JobResponse createJob(UUID userId, JobRequest request) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);

        if (profile.getCompany() == null) {
            throw new BadRequestException("You must create or join a company before posting a job.");
        }

        Job job = Job.builder()
                .recruiterProfile(profile)
                .company(profile.getCompany())
                .title(request.title())
                .description(request.description())
                .requirements(request.requirements())
                .location(request.location())
                .jobType(request.jobType())
                .minSalary(request.minSalary())
                .maxSalary(request.maxSalary())
                .status(JobStatus.DRAFT)
                .build();

        return toResponseWithCount(jobRepository.save(job));
    }

    public JobResponse updateJob(UUID userId, UUID jobId, JobRequest request) {
        Job job = getOwnedJob(userId, jobId);

        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setRequirements(request.requirements());
        job.setLocation(request.location());
        job.setJobType(request.jobType());
        job.setMinSalary(request.minSalary());
        job.setMaxSalary(request.maxSalary());

        return toResponseWithCount(jobRepository.save(job));
    }

    /** Explicit status transitions (DRAFT -> OPEN -> CLOSED) kept separate from
     *  the general update, since publishing/closing a job is a distinct action
     *  from editing its content. */
    public JobResponse updateJobStatus(UUID userId, UUID jobId, JobStatus newStatus) {
        Job job = getOwnedJob(userId, jobId);
        job.setStatus(newStatus);
        return toResponseWithCount(jobRepository.save(job));
    }

    public void deleteJob(UUID userId, UUID jobId) {
        Job job = getOwnedJob(userId, jobId);
        job.setDeletedAt(Instant.now()); // soft delete - see Step 1/3 rationale
        jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public JobResponse getMyJob(UUID userId, UUID jobId) {
        return toResponseWithCount(getOwnedJob(userId, jobId));
    }

    @Transactional(readOnly = true)
    public Page<JobResponse> getMyJobs(UUID userId, Pageable pageable) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);
        return jobRepository.findByRecruiterProfileIdAndDeletedAtIsNull(profile.getId(), pageable)
                .map(this::toResponseWithCount);
    }

    /** Resolves the job AND verifies the calling recruiter owns it - the
     *  central ownership gate every write operation in this service goes through. */
    private Job getOwnedJob(UUID userId, UUID jobId) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);
        Job job = jobRepository.findByIdAndDeletedAtIsNull(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found."));

        if (!job.getRecruiterProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You do not have permission to manage this job.");
        }
        return job;
    }

    private JobResponse toResponseWithCount(Job job) {
        JobResponse base = jobMapper.toResponse(job);
        long count = jobApplicationRepository.countByJobId(job.getId());
        return new JobResponse(
                base.id(), base.title(), base.description(), base.requirements(), base.location(),
                base.jobType(), base.minSalary(), base.maxSalary(), base.status(),
                base.companyId(), base.companyName(), base.recruiterProfileId(), base.recruiterName(),
                count, base.createdAt());
    }
}
