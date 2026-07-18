package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.AiAnalysisResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.AiAnalysisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidate/ai")
@RequiredArgsConstructor
@Tag(name = "AI Resume Analysis")
@PreAuthorize("hasRole('CANDIDATE')")
public class AiAnalysisController {

    private final AiAnalysisService aiAnalysisService;

    @PostMapping("/resumes/{resumeId}/skills")
    public ApiResponse<AiAnalysisResponse> extractSkills(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId
    ) {
        return ApiResponse.success("Skills extracted.", aiAnalysisService.extractSkills(principal.getId(), resumeId));
    }

    @PostMapping("/resumes/{resumeId}/summary")
    public ApiResponse<AiAnalysisResponse> generateSummary(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId
    ) {
        return ApiResponse.success("Summary generated.", aiAnalysisService.generateSkillSummary(principal.getId(), resumeId));
    }

    @PostMapping("/resumes/{resumeId}/jobs/{jobId}/match-score")
    public ApiResponse<AiAnalysisResponse> matchScore(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId, @PathVariable UUID jobId
    ) {
        return ApiResponse.success("Match score computed.", aiAnalysisService.computeJobMatchScore(principal.getId(), resumeId, jobId));
    }

    @PostMapping("/resumes/{resumeId}/jobs/{jobId}/missing-skills")
    public ApiResponse<AiAnalysisResponse> missingSkills(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId, @PathVariable UUID jobId
    ) {
        return ApiResponse.success("Missing skills identified.", aiAnalysisService.detectMissingSkills(principal.getId(), resumeId, jobId));
    }

    @GetMapping("/history")
    public ApiResponse<List<AiAnalysisResponse>> getHistory(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Analysis history retrieved.", aiAnalysisService.getMyAnalysisHistory(principal.getId()));
    }
}
