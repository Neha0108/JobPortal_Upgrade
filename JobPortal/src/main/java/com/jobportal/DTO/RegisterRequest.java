package com.jobportal.DTO;

import com.jobportal.Entities.Role;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private Role role;
}