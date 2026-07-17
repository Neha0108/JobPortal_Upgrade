package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.recruiter.CompanyRequest;
import com.recruitment.platform.dto.recruiter.CompanyResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiter/company")
@RequiredArgsConstructor
@Tag(name = "Recruiter Company")
@PreAuthorize("hasRole('RECRUITER')")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createMyCompany(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CompanyRequest request
    ) {
        CompanyResponse response = companyService.createMyCompany(principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Company created.", response));
    }

    @GetMapping
    public ApiResponse<CompanyResponse> getMyCompany(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Company retrieved.", companyService.getMyCompany(principal.getId()));
    }

    @PutMapping
    public ApiResponse<CompanyResponse> updateMyCompany(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CompanyRequest request
    ) {
        return ApiResponse.success("Company updated.", companyService.updateMyCompany(principal.getId(), request));
    }
}