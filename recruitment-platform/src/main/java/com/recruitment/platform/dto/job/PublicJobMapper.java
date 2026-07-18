package com.recruitment.platform.dto.job;

import com.recruitment.platform.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicJobMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    PublicJobResponse toResponse(Job job);
}
