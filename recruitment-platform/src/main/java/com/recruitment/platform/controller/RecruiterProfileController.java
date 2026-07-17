package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.recruiter.RecruiterProfileResponse;
import com.recruitment.platform.dto.recruiter.UpdateRecruiterProfileRequest;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.RecruiterProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiter/profile")
@RequiredArgsConstructor
@Tag(name = "Recruiter Profile")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    @GetMapping
    public ApiResponse<RecruiterProfileResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Profile retrieved.", recruiterProfileService.getMyProfile(principal.getId()));
    }

    @PutMapping
    public ApiResponse<RecruiterProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateRecruiterProfileRequest request
    ) {
        return ApiResponse.success("Profile updated.", recruiterProfileService.updateMyProfile(principal.getId(), request));
    }
}