package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.common.PageResponse;
import com.recruitment.platform.dto.savedjob.SavedJobResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.SavedJobService;
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
@RequestMapping("/api/v1/candidate/saved-jobs")
@RequiredArgsConstructor
@Tag(name = "Candidate Saved Jobs")
@PreAuthorize("hasRole('CANDIDATE')")
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse<SavedJobResponse>> save(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID jobId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job saved.", savedJobService.saveJob(principal.getId(), jobId)));
    }

    @DeleteMapping("/{jobId}")
    public ApiResponse<Void> unsave(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID jobId) {
        savedJobService.unsaveJob(principal.getId(), jobId);
        return ApiResponse.success("Job removed from saved list.");
    }

    @GetMapping
    public ApiResponse<PageResponse<SavedJobResponse>> getMySavedJobs(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ApiResponse.success("Saved jobs retrieved.",
                PageResponse.from(savedJobService.getMySavedJobs(principal.getId(), pageable)));
    }
}
