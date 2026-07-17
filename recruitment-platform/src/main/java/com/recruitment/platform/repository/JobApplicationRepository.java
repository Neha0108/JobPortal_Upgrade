package com.recruitment.platform.repository;

import com.recruitment.platform.entity.ApplicationStatus;
import com.recruitment.platform.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    // Recruiter side: view/filter applicants for a job they posted
    Page<JobApplication> findByJobId(UUID jobId, Pageable pageable);

    Page<JobApplication> findByJobIdAndStatus(UUID jobId, ApplicationStatus status, Pageable pageable);

    // Candidate side: view own applications
    Page<JobApplication> findByCandidateProfileId(UUID candidateProfileId, Pageable pageable);

    Optional<JobApplication> findByJobIdAndCandidateProfileId(UUID jobId, UUID candidateProfileId);

    boolean existsByJobIdAndCandidateProfileId(UUID jobId, UUID candidateProfileId);

    long countByJobId(UUID jobId);

    long countByJobIdAndStatus(UUID jobId, ApplicationStatus status);

    long countByJob_RecruiterProfile_Id(UUID recruiterProfileId);

    long countByCandidateProfileId(UUID candidateProfileId);

    long countByCandidateProfileIdAndStatus(UUID candidateProfileId, ApplicationStatus status);

    long countByJob_RecruiterProfile_IdAndStatus(UUID recruiterProfileId, ApplicationStatus status);

    // For a recruiter dashboard: applications across every job the recruiter owns
    List<JobApplication> findByJob_RecruiterProfile_Id(UUID recruiterProfileId);
}
