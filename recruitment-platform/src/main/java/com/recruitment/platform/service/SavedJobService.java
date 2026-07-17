package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.DuplicateResourceException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.savedjob.SavedJobMapper;
import com.recruitment.platform.dto.savedjob.SavedJobResponse;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.entity.Job;
import com.recruitment.platform.entity.SavedJob;
import com.recruitment.platform.repository.JobRepository;
import com.recruitment.platform.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final CandidateProfileService candidateProfileService;
    private final SavedJobMapper savedJobMapper;

    public SavedJobResponse saveJob(UUID userId, UUID jobId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        Job job = jobRepository.findByIdAndDeletedAtIsNull(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found."));

        if (savedJobRepository.existsByCandidateProfileIdAndJobId(profile.getId(), jobId)) {
            throw new DuplicateResourceException("You have already saved this job.");
        }

        SavedJob savedJob = SavedJob.builder().candidateProfile(profile).job(job).build();
        return savedJobMapper.toResponse(savedJobRepository.save(savedJob));
    }

    public void unsaveJob(UUID userId, UUID jobId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        savedJobRepository.deleteByCandidateProfileIdAndJobId(profile.getId(), jobId);
    }

    @Transactional(readOnly = true)
    public Page<SavedJobResponse> getMySavedJobs(UUID userId, Pageable pageable) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return savedJobRepository.findByCandidateProfileId(profile.getId(), pageable)
                .map(savedJobMapper::toResponse);
    }
}
