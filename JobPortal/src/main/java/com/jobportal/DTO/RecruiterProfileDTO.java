package com.jobportal.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileDTO {

    private Long recruiterId;

    private String companyName;

    private String companyWebsite;

    private String companyEmail;

    private String companyPhone;

    private String companyLocation;

    private String companyIndustry;

    private Integer companySize;

    private String companyDescription;

    private String companyLogoUrl;

    private String linkedinCompanyUrl;

    private Boolean verified;
}