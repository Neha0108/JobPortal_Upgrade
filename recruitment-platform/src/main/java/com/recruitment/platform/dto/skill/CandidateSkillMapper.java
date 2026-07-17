package com.recruitment.platform.dto.skill;

import com.recruitment.platform.entity.CandidateSkill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateSkillMapper {
    SkillResponse toResponse(CandidateSkill skill);
}
