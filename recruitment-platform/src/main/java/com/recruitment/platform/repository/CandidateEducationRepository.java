package com.recruitment.platform.repository;
import com.recruitment.platform.entity.CandidateEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, UUID> {

    List<CandidateEducation> findByCandidateProfileId(UUID candidateProfileId);
}