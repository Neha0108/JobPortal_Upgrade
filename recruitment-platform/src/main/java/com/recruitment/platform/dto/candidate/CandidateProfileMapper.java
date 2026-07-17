package com.recruitment.platform.dto.candidate;

import com.recruitment.platform.entity.CandidateProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {

    @Mapping(target = "email", source = "user.email")
    CandidateProfileResponse toResponse(CandidateProfile profile);
}
