package com.jobportal.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.Entities.Application;

@Repository
public interface ApplicationRepo extends JpaRepository<Application,Long>{

    List<Application> findByCandidate_CandidateId(Long candidateId);

    Optional<Application> findByApplicationIdAndCandidate_CandidateId(Long applicationId, Long candidateId);

    boolean existsByCandidate_CandidateIdAndJob_JobId(Long candidateId, Long jobId);

    List<Application> findByJob_JobId(Long jobId);
}
