package com.recruitment.platform.service;
import com.recruitment.platform.common.exception.BadRequestException;
import com.recruitment.platform.common.exception.DuplicateResourceException;
import com.recruitment.platform.common.exception.InvalidTokenException;
import com.recruitment.platform.config.properties.EmailProperties;
import com.recruitment.platform.dto.auth.*;
import com.recruitment.platform.entity.*;
import com.recruitment.platform.repository.*;
import com.recruitment.platform.security.jwt.JwtTokenProvider;
import com.recruitment.platform.security.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final EmailProperties emailProperties;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Handles both CANDIDATE and RECRUITER self-registration - same endpoint,
     * branching only on which profile row gets created. ADMIN accounts are
     * provisioned out-of-band (e.g. a DB seed / internal tool), never through
     * this public endpoint.
     */
    public AuthResponse register(RegisterRequest request) {
        if (request.role() == Role.ADMIN) {
            throw new BadRequestException("Admin accounts cannot be self-registered.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("An account with this email already exists.");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .emailVerified(false)
                .active(true)
                .build();
        user = userRepository.save(user);

        if (request.role() == Role.CANDIDATE) {
            candidateProfileRepository.save(
                    CandidateProfile.builder()
                            .user(user)
                            .fullName(request.fullName())
                            .build());
        } else { // RECRUITER
            recruiterProfileRepository.save(
                    RecruiterProfile.builder()
                            .user(user)
                            .fullName(request.fullName())
                            .build()); // company assigned later via Recruiter module
        }

        issueEmailVerificationToken(user, request.fullName());

        return buildAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid email or password.");
        }
        if (!user.isActive()) {
            throw new BadRequestException("This account has been deactivated. Contact support.");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String rawToken = request.refreshToken();

        if (!jwtTokenProvider.isValid(rawToken) || !jwtTokenProvider.isTokenType(rawToken, TokenType.REFRESH)) {
            throw new InvalidTokenException("Refresh token is invalid or expired.");
        }

        String tokenHash = hash(rawToken);
        RefreshToken stored = refreshTokenRepository.findByTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(() -> new InvalidTokenException("Refresh token has been revoked or does not exist."));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token has expired.");
        }

        User user = stored.getUser();

        // Rotate: revoke the used refresh token, issue a brand new pair.
        // Prevents indefinite reuse of a single refresh token if it ever leaks.
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        return buildAuthResponse(user);
    }

    public void logout(RefreshTokenRequest request) {
        String tokenHash = hash(request.refreshToken());
        refreshTokenRepository.revokeByTokenHash(tokenHash);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        // Always respond as if successful regardless of whether the email
        // exists - prevents user enumeration via this endpoint.
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            String token = generateSecureToken();
            passwordResetTokenRepository.save(
                    PasswordResetToken.builder()
                            .user(user)
                            .token(token)
                            .expiresAt(Instant.now().plus(emailProperties.passwordResetTokenExpiryMinutes(), ChronoUnit.MINUTES))
                            .used(false)
                            .build());

            String recipientName = resolveDisplayName(user);
            emailService.sendPasswordResetEmail(user.getEmail(), recipientName, token);
        });
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(request.token())
                .orElseThrow(() -> new InvalidTokenException("Password reset token is invalid or already used."));

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Password reset token has expired.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        // Force re-login everywhere - a leaked/stolen session shouldn't survive
        // a password reset.
        refreshTokenRepository.revokeAllForUser(user.getId());
    }

    public void verifyEmail(VerifyEmailRequest request) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository
                .findByTokenAndUsedFalse(request.token())
                .orElseThrow(() -> new InvalidTokenException("Verification token is invalid or already used."));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Verification token has expired.");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        emailVerificationTokenRepository.save(verificationToken);
    }

    // ===================== internal helpers =====================

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .tokenHash(hash(refreshToken))
                        .expiresAt(jwtTokenProvider.getExpiration(refreshToken))
                        .revoked(false)
                        .build());

        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                accessToken,
                refreshToken,
                jwtTokenProvider.getExpiration(accessToken).toEpochMilli() - Instant.now().toEpochMilli());
    }

    private void issueEmailVerificationToken(User user, String fullName) {
        String token = generateSecureToken();
        emailVerificationTokenRepository.save(
                EmailVerificationToken.builder()
                        .user(user)
                        .token(token)
                        .expiresAt(Instant.now().plus(emailProperties.verificationTokenExpiryMinutes(), ChronoUnit.MINUTES))
                        .used(false)
                        .build());

        emailService.sendVerificationEmail(user.getEmail(), fullName, token);
    }

    private String resolveDisplayName(User user) {
        if (user.getRole() == Role.CANDIDATE) {
            return candidateProfileRepository.findByUserId(user.getId())
                    .map(CandidateProfile::getFullName)
                    .orElse(user.getEmail());
        }
        return recruiterProfileRepository.findByUserId(user.getId())
                .map(RecruiterProfile::getFullName)
                .orElse(user.getEmail());
    }

    // Password-reset/verification tokens are opaque random strings (not JWTs) -
    // simpler to invalidate (just flip `used`/delete row) and carry no claims
    // to protect, unlike access/refresh tokens which need to be stateless.
    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Refresh tokens ARE persisted, so we store a hash (never the raw JWT) -
    // same principle as passwords. Must be DETERMINISTIC (unlike BCrypt, which
    // salts randomly) since we look tokens up by exact hash match on refresh/logout.
    private String hash(String raw) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}