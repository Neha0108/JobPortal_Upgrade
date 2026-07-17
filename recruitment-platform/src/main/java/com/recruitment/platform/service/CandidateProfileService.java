package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.candidate.CandidateProfileMapper;
import com.recruitment.platform.dto.candidate.CandidateProfileResponse;
import com.recruitment.platform.dto.candidate.UpdateCandidateProfileRequest;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.repository.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final CandidateProfileMapper candidateProfileMapper;

    @Transactional(readOnly = true)
    public CandidateProfileResponse getMyProfile(UUID userId) {
        return candidateProfileMapper.toResponse(getProfileEntityByUserId(userId));
    }

    public CandidateProfileResponse updateMyProfile(UUID userId, UpdateCandidateProfileRequest request) {
        CandidateProfile profile = getProfileEntityByUserId(userId);
        profile.setFullName(request.fullName());
        profile.setPhone(request.phone());
        profile.setHeadline(request.headline());
        profile.setSummary(request.summary());
        return candidateProfileMapper.toResponse(candidateProfileRepository.save(profile));
    }

    /** Anchor point every other candidate-module service resolves ownership through. */
    @Transactional(readOnly = true)
    public CandidateProfile getProfileEntityByUserId(UUID userId) {
        return candidateProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found for this user."));
    }
}