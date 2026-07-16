package com.jobportal.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.DTO.CandidateProfileDTO;
import com.jobportal.Entities.CandidateProfile;
import com.jobportal.Entities.Skill;
import com.jobportal.Entities.Users;
import com.jobportal.Repositories.CandidateProfileRepo;
import com.jobportal.Repositories.SkillRepository;
import com.jobportal.Repositories.UsersRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateService{

    private final CandidateProfileRepo candidateProfileRepo;
    private final UsersRepo usersRepo;
    private final SkillRepository skillRepository;

    public CandidateProfileDTO createProfile(CandidateProfileDTO request, Long userId) {
        if (candidateProfileRepo.existsByUser_UserId(userId)) {
            throw new IllegalStateException("Candidate profile already exists for this user");
        }

        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        CandidateProfile profile = CandidateProfile.builder()
                .user(user)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .bio(request.getBio())
                .education(request.getEducation())
                .yearsOfExperience(request.getYearsOfExperience())
                .linkedinUrl(request.getLinkedinUrl())
                .githubUrl(request.getGithubUrl())
                .portfolioUrl(request.getPortfolioUrl())
                .resumeUrl(request.getResumeUrl())
                .profileImageUrl(request.getProfileImageUrl())
                .skills(resolveSkills(request.getSkills()))
                .build();

        return mapToDTO(candidateProfileRepo.save(profile));
    }

    public CandidateProfileDTO getProfile(Long userId) {
        return mapToDTO(getProfileEntity(userId));
    }

    public CandidateProfileDTO updateProfile(Long userId, CandidateProfileDTO request) {
        CandidateProfile profile = getProfileEntity(userId);

        profile.setFullName(request.getFullName());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setCountry(request.getCountry());
        profile.setBio(request.getBio());
        profile.setEducation(request.getEducation());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setProfileImageUrl(request.getProfileImageUrl());

        if (request.getSkills() != null) {
            profile.setSkills(resolveSkills(request.getSkills()));
        }

        return mapToDTO(candidateProfileRepo.save(profile));
    }

    private CandidateProfile getProfileEntity(Long userId) {
        return candidateProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user id: " + userId));
    }

    private Set<Skill> resolveSkills(Set<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return Set.of();
        }
        List<Skill> skills = skillRepository.findBySkillNameIn(skillNames);
        return Set.copyOf(skills);
    }

    private CandidateProfileDTO mapToDTO(CandidateProfile profile) {
        return CandidateProfileDTO.builder()
                .candidateId(profile.getCandidateId())
                .fullName(profile.getFullName())
                .phoneNumber(profile.getPhoneNumber())
                .dateOfBirth(profile.getDateOfBirth())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .bio(profile.getBio())
                .education(profile.getEducation())
                .yearsOfExperience(profile.getYearsOfExperience())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .resumeUrl(profile.getResumeUrl())
                .profileImageUrl(profile.getProfileImageUrl())
                .skills(
                        profile.getSkills() == null
                                ? Set.of()
                                : profile.getSkills()
                                .stream()
                                .map(Skill::getSkillName)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}