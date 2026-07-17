package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.common.PageResponse;
import com.recruitment.platform.dto.application.ApplyJobRequest;
import com.recruitment.platform.dto.application.CandidateApplicationResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CandidateApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
@Tag(name = "Candidate Applications")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateApplicationController {

    private final CandidateApplicationService candidateApplicationService;

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<ApiResponse<CandidateApplicationResponse>> apply(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID jobId,
            @RequestBody(required = false) ApplyJobRequest request
    ) {
        UUID resumeId = request != null ? request.resumeId() : null;
        CandidateApplicationResponse response = candidateApplicationService.applyToJob(principal.getId(), jobId, resumeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Application submitted.", response));
    }

    @GetMapping("/applications")
    public ApiResponse<PageResponse<CandidateApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ApiResponse.success("Applications retrieved.",
                PageResponse.from(candidateApplicationService.getMyApplications(principal.getId(), pageable)));
    }
}
