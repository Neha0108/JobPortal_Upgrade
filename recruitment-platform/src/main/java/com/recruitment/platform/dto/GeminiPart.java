package com.recruitment.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeminiPart(
        String text,
        @JsonProperty("inline_data") InlineData inlineData
) {
    public record InlineData(
            @JsonProperty("mime_type") String mimeType,
            String data
    ) {
    }

    public static GeminiPart ofText(String text) {
        return new GeminiPart(text, null);
    }

    public static GeminiPart ofFile(String mimeType, String base64Data) {
        return new GeminiPart(null, new InlineData(mimeType, base64Data));
    }
}
