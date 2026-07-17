package com.recruitment.platform.dto.recruiter;

import com.recruitment.platform.dto.recruiter.RecruiterProfileResponse;
import com.recruitment.platform.entity.RecruiterProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruiterProfileMapper {

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    RecruiterProfileResponse toResponse(RecruiterProfile profile);
}
