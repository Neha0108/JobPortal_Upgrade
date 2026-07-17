package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.education.CandidateEducationMapper;
import com.recruitment.platform.dto.education.EducationRequest;
import com.recruitment.platform.dto.education.EducationResponse;
import com.recruitment.platform.entity.CandidateEducation;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.repository.CandidateEducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateEducationService {

    private final CandidateEducationRepository educationRepository;
    private final CandidateProfileService candidateProfileService;
    private final CandidateEducationMapper educationMapper;

    public EducationResponse addEducation(UUID userId, EducationRequest request) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        CandidateEducation education = CandidateEducation.builder()
                .candidateProfile(profile)
                .institution(request.institution())
                .degree(request.degree())
                .fieldOfStudy(request.fieldOfStudy())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();
        return educationMapper.toResponse(educationRepository.save(education));
    }

    public EducationResponse updateEducation(UUID userId, UUID educationId, EducationRequest request) {
        CandidateEducation education = getOwnedEducation(userId, educationId);
        education.setInstitution(request.institution());
        education.setDegree(request.degree());
        education.setFieldOfStudy(request.fieldOfStudy());
        education.setStartDate(request.startDate());
        education.setEndDate(request.endDate());
        return educationMapper.toResponse(educationRepository.save(education));
    }

    public void deleteEducation(UUID userId, UUID educationId) {
        educationRepository.delete(getOwnedEducation(userId, educationId));
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getMyEducation(UUID userId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return educationRepository.findByCandidateProfileId(profile.getId()).stream()
                .map(educationMapper::toResponse).toList();
    }

    private CandidateEducation getOwnedEducation(UUID userId, UUID educationId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        CandidateEducation education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education entry not found."));
        if (!education.getCandidateProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this entry.");
        }
        return education;
    }
}
