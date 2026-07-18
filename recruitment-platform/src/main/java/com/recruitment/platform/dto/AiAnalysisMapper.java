package com.recruitment.platform.dto;

import com.recruitment.platform.entity.AiAnalysisResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AiAnalysisMapper {
    AiAnalysisResponse toResponse(AiAnalysisResult result);
}
