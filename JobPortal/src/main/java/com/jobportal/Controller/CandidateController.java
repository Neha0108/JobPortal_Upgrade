package com.jobportal.Controller;

import com.jobportal.DTO.*;
import com.jobportal.Entities.EmploymentType;
import com.jobportal.Entities.JobCategory;
import com.jobportal.Entities.WorkMode;
import com.jobportal.Security.UserPrincipal;
import com.jobportal.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CANDIDATE')")
public class CandidateController {

    private final CandidateService candidateService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final RecruiterService recruiterService;

    // ---------------- Profile ----------------

    @PostMapping("/profile")
    public ResponseEntity<CandidateProfileDTO> createProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                             @RequestBody CandidateProfileDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(candidateService.createProfile(request, principal.getUserId()));
    }

    @GetMapping("/profile")
    public ResponseEntity<CandidateProfileDTO> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(candidateService.getProfile(principal.getUserId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<CandidateProfileDTO> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                             @RequestBody CandidateProfileDTO request) {
        return ResponseEntity.ok(candidateService.updateProfile(principal.getUserId(), request));
    }

    // ---------------- Browse & search jobs ----------------

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> browseJobs(@RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String location,
                                                   @RequestParam(required = false) JobCategory category,
                                                   @RequestParam(required = false) WorkMode workMode,
                                                   @RequestParam(required = false) EmploymentType employmentType) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, location, category, workMode, employmentType));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    // ---------------- Applications ----------------

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<ApplicationDTO> applyToJob(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Long jobId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.applyToJob(jobId, principal.getUserId()));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDTO>> getMyApplications(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(applicationService.getMyApplications(principal.getUserId()));
    }

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<String> withdrawApplication(@AuthenticationPrincipal UserPrincipal principal,
                                                      @PathVariable Long applicationId) {
        applicationService.withdrawApplication(applicationId, principal.getUserId());
        return ResponseEntity.ok("Application withdrawn successfully");
    }

    // ---------------- Saved jobs ----------------

    @PostMapping("/saved-jobs/{jobId}")
    public ResponseEntity<SavedJobDTO> saveJob(@AuthenticationPrincipal UserPrincipal principal,
                                               @PathVariable Long jobId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedJobService.saveJob(jobId, principal.getUserId()));
    }

    @DeleteMapping("/saved-jobs/{jobId}")
    public ResponseEntity<String> unsaveJob(@AuthenticationPrincipal UserPrincipal principal,
                                            @PathVariable Long jobId) {
        savedJobService.unsaveJob(jobId, principal.getUserId());
        return ResponseEntity.ok("Job removed from saved list");
    }

    @GetMapping("/saved-jobs")
    public ResponseEntity<List<SavedJobDTO>> getSavedJobs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(savedJobService.getSavedJobs(principal.getUserId()));
    }

    // ---------------- Recruiter profile (view only) ----------------

    @GetMapping("/recruiters/{recruiterId}")
    public ResponseEntity<RecruiterProfileDTO> getRecruiterProfile(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(recruiterService.getRecruiterProfile(recruiterId));
    }

    @GetMapping("/jobs/all")
    public ResponseEntity<List<JobDTO>> getAllJobs() {

        return ResponseEntity.ok(jobService.getAllJobs());

    }
}