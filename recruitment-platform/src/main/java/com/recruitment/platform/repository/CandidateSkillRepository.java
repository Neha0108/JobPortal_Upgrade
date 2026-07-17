package com.recruitment.platform.repository;

import com.recruitment.platform.entity.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, UUID> {

    List<CandidateSkill> findByCandidateProfileId(UUID candidateProfileId);

    void deleteByCandidateProfileIdAndSkillNameIgnoreCase(UUID candidateProfileId, String skillName);
}
