package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.education.EducationRequest;
import com.recruitment.platform.dto.education.EducationResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CandidateEducationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidate/education")
@RequiredArgsConstructor
@Tag(name = "Candidate Education")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateEducationController {

    private final CandidateEducationService educationService;

    @PostMapping
    public ResponseEntity<ApiResponse<EducationResponse>> add(
            @AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody EducationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Education added.", educationService.addEducation(principal.getId(), request)));
    }

    @PutMapping("/{educationId}")
    public ApiResponse<EducationResponse> update(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID educationId,
            @Valid @RequestBody EducationRequest request
    ) {
        return ApiResponse.success("Education updated.", educationService.updateEducation(principal.getId(), educationId, request));
    }

    @DeleteMapping("/{educationId}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID educationId) {
        educationService.deleteEducation(principal.getId(), educationId);
        return ApiResponse.success("Education removed.");
    }

    @GetMapping
    public ApiResponse<List<EducationResponse>> getMyEducation(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Education retrieved.", educationService.getMyEducation(principal.getId()));
    }
}