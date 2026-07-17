package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.Experience.CandidateExperienceMapper;
import com.recruitment.platform.dto.Experience.ExperienceRequest;
import com.recruitment.platform.dto.Experience.ExperienceResponse;
import com.recruitment.platform.entity.CandidateExperience;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.repository.CandidateExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateExperienceService {

    private final CandidateExperienceRepository experienceRepository;
    private final CandidateProfileService candidateProfileService;
    private final CandidateExperienceMapper experienceMapper;

    public ExperienceResponse addExperience(UUID userId, ExperienceRequest request) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        CandidateExperience experience = CandidateExperience.builder()
                .candidateProfile(profile)
                .companyName(request.companyName())
                .jobTitle(request.jobTitle())
                .description(request.description())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();
        return experienceMapper.toResponse(experienceRepository.save(experience));
    }

    public ExperienceResponse updateExperience(UUID userId, UUID experienceId, ExperienceRequest request) {
        CandidateExperience experience = getOwnedExperience(userId, experienceId);
        experience.setCompanyName(request.companyName());
        experience.setJobTitle(request.jobTitle());
        experience.setDescription(request.description());
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.endDate());
        return experienceMapper.toResponse(experienceRepository.save(experience));
    }

    public void deleteExperience(UUID userId, UUID experienceId) {
        experienceRepository.delete(getOwnedExperience(userId, experienceId));
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getMyExperience(UUID userId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return experienceRepository.findByCandidateProfileId(profile.getId()).stream()
                .map(experienceMapper::toResponse).toList();
    }

    private CandidateExperience getOwnedExperience(UUID userId, UUID experienceId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        CandidateExperience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience entry not found."));
        if (!experience.getCandidateProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this entry.");
        }
        return experience;
    }
}
