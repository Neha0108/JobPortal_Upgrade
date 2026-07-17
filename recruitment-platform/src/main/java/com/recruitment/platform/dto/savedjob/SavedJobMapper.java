package com.recruitment.platform.dto.savedjob;

import com.recruitment.platform.entity.SavedJob;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SavedJobMapper {

    @Mapping(target = "savedJobId", source = "id")
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "companyName", source = "job.company.name")
    @Mapping(target = "location", source = "job.location")
    @Mapping(target = "jobType", source = "job.jobType")
    @Mapping(target = "jobStatus", source = "job.status")
    @Mapping(target = "savedAt", source = "createdAt")
    SavedJobResponse toResponse(SavedJob savedJob);
}
