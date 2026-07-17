package com.recruitment.platform.dto.job;

import com.recruitment.platform.entity.Job;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "recruiterProfileId", source = "recruiterProfile.id")
    @Mapping(target = "recruiterName", source = "recruiterProfile.fullName")
    @Mapping(target = "applicantCount", ignore = true) // set manually in service - requires a separate count query
    JobResponse toResponse(Job job);
}
