package com.jobportal.Service;

import com.jobportal.DTO.JobDTO;
import com.jobportal.Entities.*;
import com.jobportal.Repositories.JobRepo;
import com.jobportal.Repositories.RecruiterProfileRepository;
import com.jobportal.Repositories.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepo jobRepo;
    private final RecruiterProfileRepository recruiterRepo;
    private final SkillRepository skillRepo;

    public JobService(JobRepo jobRepo, RecruiterProfileRepository recruiterRepo, SkillRepository skillRepo) {
        this.jobRepo = jobRepo;
        this.recruiterRepo = recruiterRepo;
        this.skillRepo = skillRepo;
    }

    private JobDTO toResponse(Job job) {
        return JobDTO.builder()
                .jobId(job.getJobId())
                .jobTitle(job.getJobTitle())
                .jobDescription(job.getJobDescription())
                .jobLocation(job.getJobLocation())
                .employmentType(job.getEmploymentType())
                .category(job.getCategory())
                .workMode(job.getWorkMode())
                .experienceRequired(job.getExperienceRequired())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .vacancies(job.getVacancies())
                .benefits(job.getBenefits())
                .active(job.getActive())
                .expiryDate(job.getExpiryDate())
                .companyName(job.getRecruiter().getCompanyName())
                .skillNames(job.getRequiredSkills().stream()
                        .map(Skill::getSkillName)
                        .collect(Collectors.toSet()))
                .createdAt(job.getCreatedAt())
                .build();
    }

    public JobDTO addJob(JobDTO request, Long userId) {

        RecruiterProfile recruiter = recruiterRepo.findByUserUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

        Set<Skill> skills = new HashSet<>(skillRepo.findAllById(request.getSkillIds()));

        Job job = Job.builder()
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .jobLocation(request.getJobLocation())
                .employmentType(request.getEmploymentType())
                .category(request.getCategory())
                .workMode(request.getWorkMode())
                .experienceRequired(request.getExperienceRequired())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .vacancies(request.getVacancies())
                .benefits(request.getBenefits())
                .expiryDate(request.getExpiryDate())
                .requiredSkills(skills)
                .active(request.getActive() != null ? request.getActive() : true)
                .recruiter(recruiter)
                .build();

        return toResponse(jobRepo.save(job));
    }

    public JobDTO updateJob(Long jobId, JobDTO request) {

        Job existingJob = jobRepo.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        Set<Skill> skills = new HashSet<>(skillRepo.findAllById(request.getSkillIds()));

        existingJob.setJobTitle(request.getJobTitle());
        existingJob.setJobDescription(request.getJobDescription());
        existingJob.setJobLocation(request.getJobLocation());
        existingJob.setEmploymentType(request.getEmploymentType());
        existingJob.setCategory(request.getCategory());
        existingJob.setWorkMode(request.getWorkMode());
        existingJob.setExperienceRequired(request.getExperienceRequired());
        existingJob.setMinSalary(request.getMinSalary());
        existingJob.setMaxSalary(request.getMaxSalary());
        existingJob.setVacancies(request.getVacancies());
        existingJob.setBenefits(request.getBenefits());
        existingJob.setExpiryDate(request.getExpiryDate());
        existingJob.setRequiredSkills(skills);

        if (request.getActive() != null) {
            existingJob.setActive(request.getActive());
        }

        return toResponse(jobRepo.save(existingJob));
    }

    // ── Delete Job ─────────────────────────────────────────────────────────────
    public void deleteJob(Long jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        jobRepo.delete(job);
    }

    // ── Get Single Job ─────────────────────────────────────────────────────────
    public JobDTO getJobById(Long jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
        return toResponse(job);
    }

    public List<JobDTO> getAllJobs() {
        return jobRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getAllActiveJobs() {
        return jobRepo.findByActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByRecruiter(Long userId) {
        RecruiterProfile recruiter = recruiterRepo.findByUserUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

        return jobRepo.findByRecruiterRecruiterId(recruiter.getRecruiterId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobDTO> searchJobs(String keyword,
                                   String location,
                                   JobCategory category,
                                   WorkMode workMode,
                                   EmploymentType employmentType) {

        return jobRepo.searchJobs(
                        keyword,
                        location,
                        category,
                        workMode,
                        employmentType)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}