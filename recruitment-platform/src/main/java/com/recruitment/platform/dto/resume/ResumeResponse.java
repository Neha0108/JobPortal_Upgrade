package com.recruitment.platform.dto.resume;

import java.time.Instant;
import java.util.UUID;

public record ResumeResponse(
        UUID id,
        String fileName,
        String fileType,
        long fileSize,
        boolean primary,
        Instant uploadedAt
) {
}