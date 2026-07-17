package com.recruitment.platform.dto.application;

import com.recruitment.platform.entity.JobApplication;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateApplicationMapper {

    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "companyName", source = "job.company.name")
    @Mapping(target = "resumeId", source = "resume.id")
    @Mapping(target = "resumeFileName", source = "resume.fileName")
    @Mapping(target = "appliedAt", source = "createdAt")
    CandidateApplicationResponse toResponse(JobApplication application);
}