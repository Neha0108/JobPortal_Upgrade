package com.recruitment.platform.controller;

import com.recruitment.platform.common.ApiResponse;
import com.recruitment.platform.dto.skill.SkillRequest;
import com.recruitment.platform.dto.skill.SkillResponse;
import com.recruitment.platform.security.userdetails.UserPrincipal;
import com.recruitment.platform.service.CandidateSkillService;
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
@RequestMapping("/api/v1/candidate/skills")
@RequiredArgsConstructor
@Tag(name = "Candidate Skills")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateSkillController {

    private final CandidateSkillService skillService;

    @PostMapping
    public ResponseEntity<ApiResponse<SkillResponse>> add(
            @AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody SkillRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Skill added.", skillService.addSkill(principal.getId(), request)));
    }

    @DeleteMapping("/{skillId}")
    public ApiResponse<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID skillId) {
        skillService.deleteSkill(principal.getId(), skillId);
        return ApiResponse.success("Skill removed.");
    }

    @GetMapping
    public ApiResponse<List<SkillResponse>> getMySkills(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success("Skills retrieved.", skillService.getMySkills(principal.getId()));
    }
}