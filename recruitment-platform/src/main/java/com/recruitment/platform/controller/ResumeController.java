package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.resume.DownloadableFile;
import com.recruitment.platform.dto.resume.ResumeResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.ResumeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidate/resumes")
@RequiredArgsConstructor
@Tag(name = "Candidate Resumes")
@PreAuthorize("hasRole('CANDIDATE')")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResumeResponse>> upload(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("file") MultipartFile file
    ) {
        ResumeResponse response = resumeService.uploadResume(principal.getId(), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Resume uploaded.", response));
    }

    @GetMapping
    public ApiResponse<List<ResumeResponse>> getMyResumes(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Resumes retrieved.", resumeService.getMyResumes(principal.getId()));
    }

    @PatchMapping("/{resumeId}/primary")
    public ApiResponse<ResumeResponse> setPrimary(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId
    ) {
        return ApiResponse.success("Primary resume updated.", resumeService.setPrimaryResume(principal.getId(), resumeId));
    }

    @DeleteMapping("/{resumeId}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId) {
        resumeService.deleteResume(principal.getId(), resumeId);
        return ApiResponse.success("Resume deleted.");
    }

    @GetMapping("/{resumeId}/download")
    public ResponseEntity<org.springframework.core.io.Resource> download(
            @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID resumeId
    ) {
        DownloadableFile file = resumeService.downloadResume(principal.getId(), resumeId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
                .body(file.resource());
    }
}
