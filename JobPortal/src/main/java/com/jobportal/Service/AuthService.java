package com.jobportal.Service;

import com.jobportal.DTO.AuthResponse;
import com.jobportal.DTO.LoginRequest;
import com.jobportal.DTO.RegisterRequest;
import com.jobportal.Entities.*;
import com.jobportal.Repositories.CandidateProfileRepository;
import com.jobportal.Repositories.RecruiterProfileRepository;
import com.jobportal.Repositories.UsersRepo;
import com.jobportal.Security.JwtService;
import com.jobportal.Security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CandidateProfileRepository candidateProfileRepo;
    private final RecruiterProfileRepository recruiterProfileRepo;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already in use");

        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already taken");

        Users user = Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.CANDIDATE)
                .enabled(true)
                .build();

        Users savedUser = userRepository.save(user);

        if(savedUser.getRole() == Role.CANDIDATE) {

            CandidateProfile candidateProfile = CandidateProfile.builder().user(savedUser).fullName(savedUser.getUsername()).build();
            candidateProfileRepo.save(candidateProfile);
        }

        if(savedUser.getRole() == Role.RECRUITER) {
            RecruiterProfile recruiterProfile = RecruiterProfile.builder().user(savedUser).companyName("Complete Company Profile").verified(false).build();
            recruiterProfileRepo.save(recruiterProfile);
        }

        String accessToken   = jwtService.generateAccessToken(savedUser);
        RefreshToken refresh = refreshTokenService.createRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refresh.getToken())
                .role(savedUser.getRole().name())
                .userId(savedUser.getUserId())          // ✅ now not null
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid email or password");

        if (!user.getEnabled())
            throw new RuntimeException("Account is disabled");

        String accessToken   = jwtService.generateAccessToken(user);
        RefreshToken refresh = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refresh.getToken())
                .role(user.getRole().name())
                .userId(user.getUserId())
                .build();
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenService.verifyRefreshToken(refreshToken);
        Users user = token.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(token.getToken())  // same refresh token
                .role(user.getRole().name())
                .userId(user.getUserId())
                .build();
    }

    public void logout(Users user) {
        refreshTokenService.revokeRefreshToken(user);
    }
}