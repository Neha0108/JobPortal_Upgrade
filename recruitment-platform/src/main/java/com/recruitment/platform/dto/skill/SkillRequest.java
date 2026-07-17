package com.recruitment.platform.dto.skill;

import com.recruitment.platform.entity.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;

public record SkillRequest(
        @NotBlank String skillName,
        ProficiencyLevel proficiencyLevel
) {
}