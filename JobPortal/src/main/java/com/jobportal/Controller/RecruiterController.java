package com.jobportal.Controller;

import com.jobportal.DTO.JobRequest;
import com.jobportal.DTO.JobResponse;
import com.jobportal.Entities.Job;
import com.jobportal.Security.UserPrincipal;
import com.jobportal.Service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final JobService jobService;

    @PostMapping("/jobs")
    public ResponseEntity<JobResponse> createJob(@AuthenticationPrincipal UserPrincipal principal, @RequestBody JobRequest job) {
        System.out.println("Principal= " +principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.addJob(job, principal.getUserId()));
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<Job> getJobById(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                jobService.getJobById(jobId)
        );
    }

    @PutMapping("/jobs/{jobId}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long jobId,
            @RequestBody Job job) {

        return ResponseEntity.ok(
                jobService.updateJob(jobId, job)
        );
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long jobId) {

        jobService.deleteJob(jobId);

        return ResponseEntity.ok(
                "Job deleted successfully"
        );
    }
}