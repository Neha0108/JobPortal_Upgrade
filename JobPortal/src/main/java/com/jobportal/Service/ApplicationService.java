package com.jobportal.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.DTO.ApplicationDTO;
import com.jobportal.Entities.Application;
import com.jobportal.Entities.ApplicationStatus;
import com.jobportal.Entities.CandidateProfile;
import com.jobportal.Entities.Job;
import com.jobportal.Repositories.ApplicationRepo;
import com.jobportal.Repositories.CandidateProfileRepo;
import com.jobportal.Repositories.JobRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService{

    private final ApplicationRepo applicationRepo;
    private final CandidateProfileRepo candidateProfileRepo;
    private final JobRepo jobRepo;

    public ApplicationDTO applyToJob(Long jobId, Long userId) {
        CandidateProfile candidate = candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + jobId));

        if (applicationRepo.existsByCandidate_CandidateIdAndJob_JobId(candidate.getCandidateId(), jobId)) {
            throw new IllegalStateException("You have already applied to this job");
        }

        Application application = Application.builder()
                .candidate(candidate)
                .job(job)
                .status(ApplicationStatus.APPLIED)
                .build();

        return mapToDTO(applicationRepo.save(application));
    }

    public List<ApplicationDTO> getMyApplications(Long userId) {
        CandidateProfile candidate = candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));

        return applicationRepo.findByCandidate_CandidateId(candidate.getCandidateId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void withdrawApplication(Long applicationId, Long userId) {
        CandidateProfile candidate = candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));

        Application application = applicationRepo
                .findByApplicationIdAndCandidate_CandidateId(applicationId, candidate.getCandidateId())
                .orElseThrow(() -> new EntityNotFoundException("Application not found with id: " + applicationId));

        applicationRepo.delete(application);
    }

    private ApplicationDTO mapToDTO(Application application) {
        return ApplicationDTO.builder()
                .applicationId(application.getApplicationId())
                .jobId(application.getJob().getJobId())
                .jobTitle(application.getJob().getJobTitle())
                .candidateId(application.getCandidate().getCandidateId())
                .candidateName(application.getCandidate().getFullName())
                .status(application.getStatus())
                .recruiterRemarks(application.getRecruiterRemarks())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}