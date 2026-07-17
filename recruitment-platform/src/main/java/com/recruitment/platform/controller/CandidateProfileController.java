package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.candidate.CandidateProfileResponse;
import com.recruitment.platform.dto.candidate.UpdateCandidateProfileRequest;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CandidateProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidate/profile")
@RequiredArgsConstructor
@Tag(name = "Candidate Profile")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @GetMapping
    public ApiResponse<CandidateProfileResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Profile retrieved.", candidateProfileService.getMyProfile(principal.getId()));
    }

    @PutMapping
    public ApiResponse<CandidateProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateCandidateProfileRequest request
    ) {
        return ApiResponse.success("Profile updated.", candidateProfileService.updateMyProfile(principal.getId(), request));
    }
}