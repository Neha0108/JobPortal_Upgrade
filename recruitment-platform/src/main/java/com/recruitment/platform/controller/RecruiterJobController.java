package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.common.PageResponse;
import com.recruitment.platform.dto.job.JobRequest;
import com.recruitment.platform.dto.job.JobResponse;
import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.RecruiterJobService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/recruiter/jobs")
@RequiredArgsConstructor
@Tag(name = "Recruiter Jobs")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterJobController {

    private final RecruiterJobService recruiterJobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody JobRequest request
    ) {
        JobResponse response = recruiterJobService.createJob(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Job created as draft.", response));
    }

    @PutMapping("/{jobId}")
    public ApiResponse<JobResponse> updateJob(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID jobId,
            @Valid @RequestBody JobRequest request
    ) {
        return ApiResponse.success("Job updated.", recruiterJobService.updateJob(principal.getId(), jobId, request));
    }

    @PatchMapping("/{jobId}/status")
    public ApiResponse<JobResponse> updateJobStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID jobId,
            @RequestParam JobStatus status
    ) {
        return ApiResponse.success("Job status updated.", recruiterJobService.updateJobStatus(principal.getId(), jobId, status));
    }

    @DeleteMapping("/{jobId}")
    public ApiResponse<Void> deleteJob(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID jobId) {
        recruiterJobService.deleteJob(principal.getId(), jobId);
        return ApiResponse.success("Job deleted.");
    }

    @GetMapping("/{jobId}")
    public ApiResponse<JobResponse> getMyJob(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID jobId) {
        return ApiResponse.success("Job retrieved.", recruiterJobService.getMyJob(principal.getId(), jobId));
    }

    @GetMapping
    public ApiResponse<PageResponse<JobResponse>> getMyJobs(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ApiResponse.success("Jobs retrieved.",
                PageResponse.from(recruiterJobService.getMyJobs(principal.getId(), pageable)));
    }
}
