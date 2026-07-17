package com.recruitment.platform.security.userdetails;

import com.recruitment.platform.entity.Role;
import com.recruitment.platform.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Spring Security's view of a User. Two construction paths:
 *  - fromUser(User): used ONLY during login, carries the real password hash
 *    so Spring Security's DaoAuthenticationProvider can verify credentials.
 *  - fromJwtClaims(...): used by JwtAuthenticationFilter on every subsequent
 *    request. No password (not needed - the request is already proven
 *    authentic by the JWT signature), no DB call.
 */
@Getter
@Builder
public class UserPrincipal implements UserDetails {

    private final UUID id;
    private final String email;
    private final String password; // null when built from JWT claims
    private final Role role;
    private final boolean active;

    public static UserPrincipal fromUser(User user) {
        return UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }

    public static UserPrincipal fromJwtClaims(UUID id, String email, Role role) {
        return UserPrincipal.builder()
                .id(id)
                .email(email)
                .password(null)
                .role(role)
                .active(true) // token was only issued to an active user at auth time
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // "ROLE_" prefix is what hasRole("RECRUITER") etc. expect under the hood.
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}