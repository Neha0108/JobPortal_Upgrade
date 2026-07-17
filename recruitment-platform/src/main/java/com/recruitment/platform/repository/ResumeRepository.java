package com.recruitment.platform.repository;

import com.recruitment.platform.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {

    List<Resume> findByCandidateProfileId(UUID candidateProfileId);

    Optional<Resume> findByCandidateProfileIdAndPrimaryTrue(UUID candidateProfileId);

    long countByCandidateProfileId(UUID candidateProfileId);

    // Used before setting a new primary resume so only one stays flagged as primary.
    @Modifying
    @Query("UPDATE Resume r SET r.primary = false WHERE r.candidateProfile.id = :candidateProfileId")
    void clearPrimaryFlagForCandidate(@Param("candidateProfileId") UUID candidateProfileId);
}
