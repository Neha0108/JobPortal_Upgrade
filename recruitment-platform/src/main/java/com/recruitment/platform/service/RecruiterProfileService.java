package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.recruiter.RecruiterProfileResponse;
import com.recruitment.platform.dto.recruiter.UpdateRecruiterProfileRequest;
import com.recruitment.platform.entity.RecruiterProfile;
import com.recruitment.platform.dto.recruiter.RecruiterProfileMapper;
import com.recruitment.platform.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final RecruiterProfileMapper recruiterProfileMapper;

    @Transactional(readOnly = true)
    public RecruiterProfileResponse getMyProfile(UUID userId) {
        return recruiterProfileMapper.toResponse(getProfileEntityByUserId(userId));
    }

    public RecruiterProfileResponse updateMyProfile(UUID userId, UpdateRecruiterProfileRequest request) {
        RecruiterProfile profile = getProfileEntityByUserId(userId);
        profile.setFullName(request.fullName());
        profile.setPhone(request.phone());
        profile.setDesignation(request.designation());
        return recruiterProfileMapper.toResponse(recruiterProfileRepository.save(profile));
    }

    /**
     * Used by other recruiter-module services (Company, Job, Application) to
     * resolve "which RecruiterProfile does this authenticated user own" -
     * the anchor point every ownership check in this module builds on.
     */
    @Transactional(readOnly = true)
    public RecruiterProfile getProfileEntityByUserId(UUID userId) {
        return recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for this user."));
    }
}
