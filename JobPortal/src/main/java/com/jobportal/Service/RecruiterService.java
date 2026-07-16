package com.jobportal.Service;

import com.jobportal.DTO.RecruiterProfileDTO;
import com.jobportal.Entities.RecruiterProfile;
import com.jobportal.Repositories.RecruiterProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final RecruiterProfileRepository recruiterProfileRepo;

    public RecruiterProfileDTO getRecruiterProfile(Long recruiterId) {

        RecruiterProfile recruiter = recruiterProfileRepo.findById(recruiterId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Recruiter not found with id: " + recruiterId));

        return RecruiterProfileDTO.builder()
                .recruiterId(recruiter.getRecruiterId())
                .companyName(recruiter.getCompanyName())
                .companyWebsite(recruiter.getCompanyWebsite())
                .companyEmail(recruiter.getCompanyEmail())
                .companyPhone(recruiter.getCompanyPhone())
                .companyLocation(recruiter.getCompanyLocation())
                .companyIndustry(recruiter.getCompanyIndustry())
                .companySize(recruiter.getCompanySize())
                .companyDescription(recruiter.getCompanyDescription())
                .companyLogoUrl(recruiter.getCompanyLogoUrl())
                .linkedinCompanyUrl(recruiter.getLinkedinCompanyUrl())
                .verified(recruiter.getVerified())
                .build();
    }
}
