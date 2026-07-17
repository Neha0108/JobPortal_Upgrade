package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.Experience.ExperienceRequest;
import com.recruitment.platform.dto.Experience.ExperienceResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CandidateExperienceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidate/experience")
@RequiredArgsConstructor
@Tag(name = "Candidate Experience")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateExperienceController {

    private final CandidateExperienceService experienceService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExperienceResponse>> add(
            @AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody ExperienceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Experience added.", experienceService.addExperience(principal.getId(), request)));
    }

    @PutMapping("/{experienceId}")
    public ApiResponse<ExperienceResponse> update(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID experienceId,
            @Valid @RequestBody ExperienceRequest request
    ) {
        return ApiResponse.success("Experience updated.", experienceService.updateExperience(principal.getId(), experienceId, request));
    }

    @DeleteMapping("/{experienceId}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID experienceId) {
        experienceService.deleteExperience(principal.getId(), experienceId);
        return ApiResponse.success("Experience removed.");
    }

    @GetMapping
    public ApiResponse<List<ExperienceResponse>> getMyExperience(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Experience retrieved.", experienceService.getMyExperience(principal.getId()));
    }
}
