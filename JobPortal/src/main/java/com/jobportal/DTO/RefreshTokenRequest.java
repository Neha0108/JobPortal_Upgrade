package com.jobportal.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RefreshTokenRequest {
    private String refreshToken;
}
