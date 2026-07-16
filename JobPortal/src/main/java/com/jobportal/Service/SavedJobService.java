package com.jobportal.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.DTO.SavedJobDTO;
import com.jobportal.Entities.CandidateProfile;
import com.jobportal.Entities.Job;
import com.jobportal.Entities.SavedJob;
import com.jobportal.Repositories.CandidateProfileRepo;
import com.jobportal.Repositories.JobRepo;
import com.jobportal.Repositories.SavedJobRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepo;
    private final CandidateProfileRepo candidateProfileRepo;
    private final JobRepo jobRepo;

    public SavedJobDTO saveJob(Long jobId, Long userId) {
        CandidateProfile candidate = candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));

        if (savedJobRepo.existsByCandidate_CandidateIdAndJob_JobId(candidate.getCandidateId(), jobId)) {
            throw new IllegalStateException("Job is already saved");
        }

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + jobId));

        SavedJob savedJob = SavedJob.builder()
                .candidate(candidate)
                .job(job)
                .build();

        return mapToDTO(savedJobRepo.save(savedJob));
    }

    public void unsaveJob(Long jobId, Long userId) {
        CandidateProfile candidate = candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));

        SavedJob savedJob = savedJobRepo.findByCandidate_CandidateIdAndJob_JobId(candidate.getCandidateId(), jobId)
                .orElseThrow(() -> new EntityNotFoundException("Saved job not found"));

        savedJobRepo.delete(savedJob);
    }

    public List<SavedJobDTO> getSavedJobs(Long userId) {
        CandidateProfile candidate = candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));

        return savedJobRepo.findByCandidate_CandidateId(candidate.getCandidateId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SavedJobDTO mapToDTO(SavedJob savedJob) {
        return SavedJobDTO.builder()
                .savedJobId(savedJob.getSavedJobId())
                .jobId(savedJob.getJob().getJobId())
                .jobTitle(savedJob.getJob().getJobTitle())
                .jobLocation(savedJob.getJob().getJobLocation())
                .savedAt(savedJob.getSavedAt())
                .build();
    }
}