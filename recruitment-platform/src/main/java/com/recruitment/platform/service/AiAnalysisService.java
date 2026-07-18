package com.recruitment.platform.service;

import com.recruitment.platform.GeminiClient.GeminiClient;
import com.recruitment.platform.common.exception.AiAnalysisException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.config.RedisConfig;
import com.recruitment.platform.dto.AiAnalysisMapper;
import com.recruitment.platform.dto.AiAnalysisResponse;
import com.recruitment.platform.entity.*;
import com.recruitment.platform.repository.AiAnalysisResultRepository;
import com.recruitment.platform.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AiAnalysisService {

    private final GeminiClient geminiClient;
    private final StorageService storageService;
    private final ResumeService resumeService;
    private final JobRepository jobRepository;
    private final CandidateProfileService candidateProfileService;
    private final AiAnalysisResultRepository aiAnalysisResultRepository;
    private final AiAnalysisMapper aiAnalysisMapper;

    @Cacheable(cacheNames = RedisConfig.CACHE_AI_ANALYSIS, key = "'skills_' + #resumeId")
    public AiAnalysisResponse extractSkills(UUID userId, UUID resumeId) {
        Resume resume = resumeService.getOwnedResume(userId, resumeId);

        String prompt = """
                You are a resume-parsing assistant. Extract every professional skill,
                technology, tool, and framework explicitly mentioned in the attached resume.
                Respond ONLY with valid JSON in this exact shape, no markdown, no commentary:
                {"skills": ["skill one", "skill two", ...]}
                """;

        String resultJson = callGeminiWithResume(prompt, resume);
        return persistResult(resume.getCandidateProfile(), resume, null, AnalysisType.SKILL_EXTRACTION, resultJson, null);
    }

    @Cacheable(cacheNames = RedisConfig.CACHE_AI_ANALYSIS, key = "'summary_' + #resumeId")
    public AiAnalysisResponse generateSkillSummary(UUID userId, UUID resumeId) {
        Resume resume = resumeService.getOwnedResume(userId, resumeId);

        String prompt = """
                You are a career advisor. Read the attached resume and write a concise,
                3-4 sentence professional summary of this candidate's skill set, seniority
                level, and area of strength, suitable for a recruiter skimming a shortlist.
                Respond ONLY with valid JSON in this exact shape, no markdown, no commentary:
                {"summary": "..."}
                """;

        String resultJson = callGeminiWithResume(prompt, resume);
        return persistResult(resume.getCandidateProfile(), resume, null, AnalysisType.SKILL_SUMMARY, resultJson, null);
    }

    @Cacheable(cacheNames = RedisConfig.CACHE_AI_ANALYSIS, key = "'match_' + #resumeId + '_' + #jobId")
    public AiAnalysisResponse computeJobMatchScore(UUID userId, UUID resumeId, UUID jobId) {
        Resume resume = resumeService.getOwnedResume(userId, resumeId);
        Job job = getJob(jobId);

        String prompt = """
                You are a recruitment matching assistant. Compare the attached resume
                against the following job posting and estimate how well this candidate
                matches, as a percentage from 0 to 100.

                JOB TITLE: %s
                JOB DESCRIPTION: %s
                JOB REQUIREMENTS: %s

                Respond ONLY with valid JSON in this exact shape, no markdown, no commentary:
                {"matchScore": <number 0-100>, "reasoning": "1-2 sentence explanation"}
                """.formatted(job.getTitle(), job.getDescription(),
                job.getRequirements() != null ? job.getRequirements() : "Not specified");

        String resultJson = callGeminiWithResume(prompt, resume);
        BigDecimal matchScore = extractMatchScore(resultJson);
        return persistResult(resume.getCandidateProfile(), resume, job, AnalysisType.JOB_MATCH_SCORE, resultJson, matchScore);
    }

    @Cacheable(cacheNames = RedisConfig.CACHE_AI_ANALYSIS, key = "'missing_' + #resumeId + '_' + #jobId")
    public AiAnalysisResponse detectMissingSkills(UUID userId, UUID resumeId, UUID jobId) {
        Resume resume = resumeService.getOwnedResume(userId, resumeId);
        Job job = getJob(jobId);

        String prompt = """
                You are a recruitment matching assistant. Compare the attached resume
                against the following job posting and list the skills or qualifications
                the job requires that are NOT evident anywhere in the resume.

                JOB TITLE: %s
                JOB DESCRIPTION: %s
                JOB REQUIREMENTS: %s

                Respond ONLY with valid JSON in this exact shape, no markdown, no commentary:
                {"missingSkills": ["skill one", "skill two", ...]}
                """.formatted(job.getTitle(), job.getDescription(),
                job.getRequirements() != null ? job.getRequirements() : "Not specified");

        String resultJson = callGeminiWithResume(prompt, resume);
        return persistResult(resume.getCandidateProfile(), resume, job, AnalysisType.MISSING_SKILLS, resultJson, null);
    }

    @Transactional(readOnly = true)
    public List<AiAnalysisResponse> getMyAnalysisHistory(UUID userId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return aiAnalysisResultRepository.findByCandidateProfileId(profile.getId()).stream()
                .map(aiAnalysisMapper::toResponse).toList();
    }

    // ===================== internal helpers =====================

    private String callGeminiWithResume(String prompt, Resume resume) {
        byte[] fileBytes;
        try {
            fileBytes = storageService.loadAsResource(resume.getFilePath()).getInputStream().readAllBytes();
        } catch (IOException e) {
            throw new AiAnalysisException("Failed to read resume file for analysis.", e);
        }
        return geminiClient.generate(prompt, fileBytes, resume.getFileType());
    }

    private Job getJob(UUID jobId) {
        return jobRepository.findByIdAndDeletedAtIsNull(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found."));
    }

    private AiAnalysisResponse persistResult(
            CandidateProfile candidateProfile, Resume resume, Job job,
            AnalysisType type, String resultJson, BigDecimal matchScore
    ) {
        AiAnalysisResult result = AiAnalysisResult.builder()
                .candidateProfile(candidateProfile)
                .resume(resume)
                .job(job)
                .analysisType(type)
                .resultJson(resultJson)
                .matchScore(matchScore)
                .build();
        return aiAnalysisMapper.toResponse(aiAnalysisResultRepository.save(result));
    }

    // Gemini is instructed to return JSON but LLM output is never 100%
    // guaranteed well-formed - parse defensively rather than trusting it blindly.
    private BigDecimal extractMatchScore(String resultJson) {
        try {
            var node = new tools.jackson.databind.json.JsonMapper().readTree(resultJson);
            return new BigDecimal(node.get("matchScore").asText());
        } catch (Exception e) {
            throw new AiAnalysisException("Failed to parse match score from AI response.", e);
        }
    }
}