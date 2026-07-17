package com.recruitment.platform.dto.auth;

import com.recruitment.platform.entity.Role;

import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        Role role,
        String accessToken,
        String refreshToken,
        long expiresInMs
) {
}
