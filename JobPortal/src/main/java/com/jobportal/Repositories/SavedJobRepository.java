package com.jobportal.Repositories;

import com.jobportal.Entities.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByCandidate_CandidateId(Long candidateId);

    Optional<SavedJob> findByCandidate_CandidateIdAndJob_JobId(Long candidateId, Long jobId);

    boolean existsByCandidate_CandidateIdAndJob_JobId(Long candidateId, Long jobId);
}
