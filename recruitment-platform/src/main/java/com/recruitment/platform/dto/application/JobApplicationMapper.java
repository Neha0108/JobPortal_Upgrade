package com.recruitment.platform.dto.application;

import com.recruitment.platform.entity.JobApplication;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "candidateProfileId", source = "candidateProfile.id")
    @Mapping(target = "candidateName", source = "candidateProfile.fullName")
    @Mapping(target = "candidateEmail", source = "candidateProfile.user.email")
    @Mapping(target = "resumeId", source = "resume.id")
    @Mapping(target = "resumeFileName", source = "resume.fileName")
    @Mapping(target = "appliedAt", source = "createdAt")
    ApplicantResponse toResponse(JobApplication application);
}
