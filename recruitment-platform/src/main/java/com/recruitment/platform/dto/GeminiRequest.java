package com.recruitment.platform.dto;

import java.util.List;

public record GeminiRequest(List<GeminiContent> contents, GenerationConfig generationConfig) {
    public record GenerationConfig(String responseMimeType) {
    }
}