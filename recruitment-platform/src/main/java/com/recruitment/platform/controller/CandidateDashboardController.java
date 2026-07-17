package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.candidate.CandidateDashboardResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CandidateDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/candidate/dashboard")
@RequiredArgsConstructor
@Tag(name = "Candidate Dashboard")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateDashboardController {

    private final CandidateDashboardService candidateDashboardService;

    @GetMapping
    public ApiResponse<CandidateDashboardResponse> getMyDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Dashboard retrieved.", candidateDashboardService.getMyDashboard(principal.getId()));
    }
}