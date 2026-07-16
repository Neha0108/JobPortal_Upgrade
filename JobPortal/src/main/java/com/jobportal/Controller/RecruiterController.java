package com.jobportal.Controller;

import com.jobportal.DTO.JobDTO;
import com.jobportal.Entities.Skill;
import com.jobportal.Repositories.SkillRepository;
import com.jobportal.Security.UserPrincipal;
import com.jobportal.Service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('RECRUITER')")
public class RecruiterController {

    private final JobService jobService;

    @PostMapping("/jobs")
    public ResponseEntity<JobDTO> createJob(@AuthenticationPrincipal UserPrincipal principal, @RequestBody JobDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.addJob(request, principal.getUserId()));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @PutMapping("/jobs/{jobId}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long jobId, @RequestBody JobDTO request) {
        return ResponseEntity.ok(jobService.updateJob(jobId, request));
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId) {jobService.deleteJob(jobId);
        return ResponseEntity.ok("Job deleted successfully");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> getMyJobs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(principal.getUserId()));
    }
}