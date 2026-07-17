package com.recruitment.platform.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.file-storage")
public record FileStorageProperties(
        String strategy,
        Local local,
        List<String> allowedContentTypes,
        long maxFileSizeBytes
) {
    public record Local(String basePath) {
    }
}
