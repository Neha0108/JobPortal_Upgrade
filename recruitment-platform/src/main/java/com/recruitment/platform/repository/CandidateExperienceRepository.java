package com.recruitment.platform.repository;

import com.recruitment.platform.entity.CandidateExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateExperienceRepository extends JpaRepository<CandidateExperience, UUID> {

    List<CandidateExperience> findByCandidateProfileId(UUID candidateProfileId);
}
