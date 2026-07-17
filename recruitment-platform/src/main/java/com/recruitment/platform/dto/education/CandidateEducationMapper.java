package com.recruitment.platform.dto.education;

import com.recruitment.platform.entity.CandidateEducation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateEducationMapper {
    EducationResponse toResponse(CandidateEducation education);
}
