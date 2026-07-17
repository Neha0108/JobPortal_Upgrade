package com.recruitment.platform.dto.Experience;

import com.recruitment.platform.entity.CandidateExperience;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateExperienceMapper {
    ExperienceResponse toResponse(CandidateExperience experience);
}
