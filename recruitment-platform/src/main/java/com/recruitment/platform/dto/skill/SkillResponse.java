package com.recruitment.platform.dto.skill;
import com.recruitment.platform.entity.ProficiencyLevel;

import java.util.UUID;

public record SkillResponse(
        UUID id,
        String skillName,
        ProficiencyLevel proficiencyLevel
) {
}