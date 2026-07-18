package com.recruitment.platform.dto;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {
    public record Candidate(Content content) {
    }
    public record Content(List<GeminiPart> parts) {
    }
}
