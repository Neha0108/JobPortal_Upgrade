package com.recruitment.platform.service;

import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.ResourceNotFoundException;
import com.recruitment.platform.dto.recruiter.CompanyMapper;
import com.recruitment.platform.dto.recruiter.CompanyRequest;
import com.recruitment.platform.dto.recruiter.CompanyResponse;
import com.recruitment.platform.entity.Company;
import com.recruitment.platform.entity.RecruiterProfile;
import com.recruitment.platform.repository.CompanyRepository;
import com.recruitment.platform.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final RecruiterProfileService recruiterProfileService;
    private final CompanyMapper companyMapper;

    /** Creates a brand new company and links it to the calling recruiter. */
    public CompanyResponse createMyCompany(UUID userId, CompanyRequest request) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);

        if (profile.getCompany() != null) {
            throw new BadRequestException(
                    "You are already associated with a company. Update it instead, or contact an admin to change companies.");
        }

        Company company = Company.builder()
                .name(request.name())
                .description(request.description())
                .website(request.website())
                .industry(request.industry())
                .logoUrl(request.logoUrl())
                .build();
        company = companyRepository.save(company);

        profile.setCompany(company);
        recruiterProfileRepository.save(profile);

        return companyMapper.toResponse(company);
    }

    @Transactional(readOnly = true)
    public CompanyResponse getMyCompany(UUID userId) {
        return companyMapper.toResponse(resolveOwnedCompany(userId));
    }

    public CompanyResponse updateMyCompany(UUID userId, CompanyRequest request) {
        Company company = resolveOwnedCompany(userId);
        company.setName(request.name());
        company.setDescription(request.description());
        company.setWebsite(request.website());
        company.setIndustry(request.industry());
        company.setLogoUrl(request.logoUrl());
        return companyMapper.toResponse(companyRepository.save(company));
    }

    /** Looks up the calling recruiter's company, enforcing they actually have one. */
    private Company resolveOwnedCompany(UUID userId) {
        RecruiterProfile profile = recruiterProfileService.getProfileEntityByUserId(userId);
        if (profile.getCompany() == null) {
            throw new ResourceNotFoundException("You have not created or joined a company yet.");
        }
        return profile.getCompany();
    }
}