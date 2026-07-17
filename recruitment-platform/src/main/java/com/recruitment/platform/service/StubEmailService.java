package com.recruitment.platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Placeholder implementation - logs instead of sending, so Auth (Step 6) isn't
 * blocked on the full Email module (Step 12: Spring Mail + templates).
 * Same interface, same @Async method signatures - swapping this out later
 * won't require touching AuthService at all.
 */
@Service
@Slf4j
public class StubEmailService implements EmailService {

    @Override
    @Async("taskExecutor")
    public void sendVerificationEmail(String toEmail, String fullName, String verificationToken) {
        log.info("[STUB EMAIL] Verification email for {} ({}) - token: {}", fullName, toEmail, verificationToken);
    }

    @Override
    @Async("taskExecutor")
    public void sendPasswordResetEmail(String toEmail, String fullName, String resetToken) {
        log.info("[STUB EMAIL] Password reset email for {} ({}) - token: {}", fullName, toEmail, resetToken);
    }
}