package com.jobportal.DTO;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfileDTO {

    private Long candidateId;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String city;
    private String state;
    private String country;
    private String bio;
    private String education;
    private Integer yearsOfExperience;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String resumeUrl;
    private String profileImageUrl;
    private Set<String> skills;
}