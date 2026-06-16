package com.jobportal.Service;

import com.jobportal.DTO.JobRequest;
import com.jobportal.DTO.JobResponse;
import com.jobportal.Entities.Job;
import com.jobportal.Entities.RecruiterProfile;
import com.jobportal.Entities.Skill;
import com.jobportal.Repositories.JobRepo;
import com.jobportal.Repositories.RecruiterProfileRepository;
import com.jobportal.Repositories.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public JobResponse addJob(JobRequest request, Long userId)
    {
        RecruiterProfile recruiter =
                recruiterRepo.findByUserUserId(userId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Recruiter not found"));

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
                .active(true)
                .recruiter(recruiter)
                .build();

        Job savedJob = jobRepo.save(job);

        return JobResponse.builder()
                .jobId(savedJob.getJobId())
                .jobTitle(savedJob.getJobTitle())
                .jobDescription(savedJob.getJobDescription())
                .companyName(savedJob.getRecruiter().getCompanyName())
                .build();
    }

    public Job updateJob(Long jobId, Job updatedJob) {

        Job existingJob = jobRepo.findById(jobId).orElseThrow(() ->
                                new EntityNotFoundException("Job not found"));

        existingJob.setJobTitle(updatedJob.getJobTitle());
        existingJob.setJobDescription(updatedJob.getJobDescription());
        existingJob.setJobLocation(updatedJob.getJobLocation());
        existingJob.setEmploymentType(updatedJob.getEmploymentType());
        existingJob.setCategory(updatedJob.getCategory());
        existingJob.setWorkMode(updatedJob.getWorkMode());
        existingJob.setExperienceRequired(updatedJob.getExperienceRequired());
        existingJob.setMinSalary(updatedJob.getMinSalary());
        existingJob.setMaxSalary(updatedJob.getMaxSalary());
        existingJob.setVacancies(updatedJob.getVacancies());
        existingJob.setBenefits(updatedJob.getBenefits());
        existingJob.setExpiryDate(updatedJob.getExpiryDate());
        existingJob.setRequiredSkills(updatedJob.getRequiredSkills());

        return jobRepo.save(existingJob);
    }

    public void deleteJob(Long jobId) {
        Job job = jobRepo.findById(jobId).orElseThrow(() ->
                                new EntityNotFoundException("Job not found"));

        jobRepo.delete(job);
    }

    public Job getJobById(Long jobId) {

        return jobRepo.findById(jobId).orElseThrow(() ->
                new EntityNotFoundException("Job not found"));
    }

    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    public List<Job> getAllActiveJobs() {
        return jobRepo.findByActiveTrue();
    }

    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return jobRepo.findByRecruiterRecruiterId(recruiterId);
    }

}
