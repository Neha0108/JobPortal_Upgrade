package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.DuplicateResourceException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.skill.CandidateSkillMapper;
import com.recruitment.platform.dto.skill.SkillRequest;
import com.recruitment.platform.dto.skill.SkillResponse;
import com.recruitment.platform.entity.CandidateProfile;
import com.recruitment.platform.entity.CandidateSkill;
import com.recruitment.platform.repository.CandidateSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateSkillService {

    private final CandidateSkillRepository skillRepository;
    private final CandidateProfileService candidateProfileService;
    private final CandidateSkillMapper skillMapper;

    public SkillResponse addSkill(UUID userId, SkillRequest request) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);

        boolean alreadyExists = skillRepository.findByCandidateProfileId(profile.getId()).stream()
                .anyMatch(s -> s.getSkillName().equalsIgnoreCase(request.skillName()));
        if (alreadyExists) {
            throw new DuplicateResourceException("This skill is already on your profile.");
        }

        CandidateSkill skill = CandidateSkill.builder()
                .candidateProfile(profile)
                .skillName(request.skillName())
                .proficiencyLevel(request.proficiencyLevel())
                .build();
        return skillMapper.toResponse(skillRepository.save(skill));
    }

    public void deleteSkill(UUID userId, UUID skillId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        CandidateSkill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found."));
        if (!skill.getCandidateProfile().getId().equals(profile.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this entry.");
        }
        skillRepository.delete(skill);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getMySkills(UUID userId) {
        CandidateProfile profile = candidateProfileService.getProfileEntityByUserId(userId);
        return skillRepository.findByCandidateProfileId(profile.getId()).stream()
                .map(skillMapper::toResponse).toList();
    }
}