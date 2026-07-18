package com.recruitment.platform.dto;

import com.recruitment.platform.entity.AnalysisType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AiAnalysisResponse(
        UUID id,
        AnalysisType analysisType,
        String resultJson,
        BigDecimal matchScore,
        Instant createdAt
) {
}