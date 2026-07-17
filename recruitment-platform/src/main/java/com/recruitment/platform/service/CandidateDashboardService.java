package com.recruitment.platform.service;

import com.recruitment.platform.dto.candidate.CandidateDashboardResponse;
import com.recruitment.platform.entity.ApplicationStatus;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.repository.JobApplicationRepository;
import com.recruitment.platform.repository.ResumeRepository;
import com.recruitment.platform.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateDashboardService {

    private final JobApplicationRepository jobApplicationRepository;
    private final SavedJobRepository savedJobRepository;
    private final ResumeRepository resumeRepository;
    private final CandidateProfileService candidateProfileService;

    public CandidateDashboardResponse getMyDashboard(UUID userId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        UUID candidateId = profile.getId();

        long total = jobApplicationRepository.countByCandidateProfileId(candidateId);
        long shortlisted = jobApplicationRepository.countByCandidateProfileIdAndStatus(candidateId, ApplicationStatus.SHORTLISTED);
        long interview = jobApplicationRepository.countByCandidateProfileIdAndStatus(candidateId, ApplicationStatus.INTERVIEW);
        long hired = jobApplicationRepository.countByCandidateProfileIdAndStatus(candidateId, ApplicationStatus.HIRED);
        long saved = savedJobRepository.countByCandidateProfileId(candidateId);
        long resumes = resumeRepository.countByCandidateProfileId(candidateId);

        return new CandidateDashboardResponse(total, shortlisted, interview, hired, saved, resumes);
    }
}
