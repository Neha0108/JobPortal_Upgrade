package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.common.PageResponse;
import com.recruitment.platform.dto.job.PublicJobResponse;
import com.recruitment.platform.entity.JobStatus;
import com.recruitment.platform.entity.JobType;
import com.recruitment.platform.service.PublicJobService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Public - no auth required (see SecurityConfig: GET /api/v1/jobs/** is permitAll). */
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Public Jobs", description = "Job browsing - no authentication required")
@SecurityRequirements
public class PublicJobController {

    private final PublicJobService publicJobService;

    @GetMapping
    public ApiResponse<PageResponse<PublicJobResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) JobStatus status,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ApiResponse.success("Jobs retrieved.",
                PageResponse.from(publicJobService.search(keyword, location, jobType, status, pageable)));
    }

    @GetMapping("/{jobId}")
    public ApiResponse<PublicJobResponse> getById(@PathVariable UUID jobId) {
        return ApiResponse.success("Job retrieved.", publicJobService.getById(jobId));
    }
}
