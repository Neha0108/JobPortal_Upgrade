package com.recruitment.platform.repository;

import com.recruitment.platform.entity.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SavedJobRepository extends JpaRepository<SavedJob, UUID> {

    Page<SavedJob> findByCandidateProfileId(UUID candidateProfileId, Pageable pageable);

    Optional<SavedJob> findByCandidateProfileIdAndJobId(UUID candidateProfileId, UUID jobId);

    boolean existsByCandidateProfileIdAndJobId(UUID candidateProfileId, UUID jobId);

    void deleteByCandidateProfileIdAndJobId(UUID candidateProfileId, UUID jobId);

    long countByCandidateProfileId(UUID candidateProfileId);
}
