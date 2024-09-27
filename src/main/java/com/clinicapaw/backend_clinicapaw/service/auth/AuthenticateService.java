package com.clinicapaw.backend_clinicapaw.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public Authentication authenticate(String username, String password) {
        log.debug("Authenticating user: {}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            log.error("Authentication failed: User not found");
            throw new BadCredentialsException("Invalid username or password");
        }

        log.debug("User authenticated successfully: {}", username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.error("Authentication failed: Incorrect password for user: {}", username);
            throw new BadCredentialsException("Incorrect Password");
        }

        log.debug("Password verified successfully for user: {}", username);

        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                userDetails.getAuthorities()
        );
    }
}
