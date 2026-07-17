package com.recruitment.platform.security.userdetails;

import com.recruitment.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Used exclusively during the login handshake (DaoAuthenticationProvider calls
 * this to fetch the password hash for comparison). Not used on subsequent
 * requests - see JwtAuthenticationFilter for why.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::fromUser)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));
    }
}