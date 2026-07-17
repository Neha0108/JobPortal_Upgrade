package com.recruitment.platform.repository;

import com.recruitment.platform.entity.AiAnalysisResult;
import com.recruitment.platform.entity.AnalysisType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AiAnalysisResultRepository extends JpaRepository<AiAnalysisResult, UUID> {

    List<AiAnalysisResult> findByCandidateProfileId(UUID candidateProfileId);

    Optional<AiAnalysisResult> findTopByResumeIdAndAnalysisTypeOrderByCreatedAtDesc(
            UUID resumeId, AnalysisType analysisType);

    Optional<AiAnalysisResult> findTopByResumeIdAndJobIdAndAnalysisTypeOrderByCreatedAtDesc(
            UUID resumeId, UUID jobId, AnalysisType analysisType);
}
