package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.recruiter.RecruiterDashboardResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.RecruiterDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recruiter/dashboard")
@RequiredArgsConstructor
@Tag(name = "Recruiter Dashboard")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterDashboardController {

    private final RecruiterDashboardService recruiterDashboardService;

    @GetMapping
    public ApiResponse<RecruiterDashboardResponse> getMyDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Dashboard retrieved.", recruiterDashboardService.getMyDashboard(principal.getId()));
    }
}
