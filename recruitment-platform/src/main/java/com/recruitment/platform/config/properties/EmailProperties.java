package com.recruitment.platform.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.email")
public record EmailProperties(
        String fromAddress,
        String fromName,
        String frontendBaseUrl,
        int verificationTokenExpiryMinutes,
        int passwordResetTokenExpiryMinutes
) {
}