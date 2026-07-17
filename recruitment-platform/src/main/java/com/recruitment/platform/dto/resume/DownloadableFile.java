package com.recruitment.platform.dto.resume;

import org.springframework.core.io.Resource;

public record DownloadableFile(Resource resource, String fileName, String contentType) {
}
