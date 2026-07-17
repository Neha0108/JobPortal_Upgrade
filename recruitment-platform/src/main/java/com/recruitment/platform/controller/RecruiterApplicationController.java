package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.common.PageResponse;
import com.recruitment.platform.dto.application.ApplicantResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.RecruiterApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
@Tag(name = "Recruiter Applications")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterApplicationController {

    private final RecruiterApplicationService recruiterApplicationService;

    @GetMapping("/jobs/{jobId}/applicants")
    public ApiResponse<PageResponse<ApplicantResponse>> getApplicants(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID jobId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ApiResponse.success("Applicants retrieved.",
                PageResponse.from(recruiterApplicationService.getApplicantsForJob(principal.getId(), jobId, pageable)));
    }

    @PatchMapping("/applications/{applicationId}/shortlist")
    public ApiResponse<ApplicantResponse> shortlist(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID applicationId
    ) {
        return ApiResponse.success("Candidate shortlisted.",
                recruiterApplicationService.shortlistCandidate(principal.getId(), applicationId));
    }

    @PatchMapping("/applications/{applicationId}/reject")
    public ApiResponse<ApplicantResponse> reject(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID applicationId
    ) {
        return ApiResponse.success("Candidate rejected.",
                recruiterApplicationService.rejectCandidate(principal.getId(), applicationId));
    }
}
