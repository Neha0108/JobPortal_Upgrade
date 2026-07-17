package com.recruitment.platform.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Type-safe binding for app.jwt.* properties.
 * Using a record here (immutable, validated at startup) rather than scattered
 * @Value injections, which are error-prone and harder to unit test.
 */
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpirationMs,
        long refreshTokenExpirationMs,
        String issuer
) {
}