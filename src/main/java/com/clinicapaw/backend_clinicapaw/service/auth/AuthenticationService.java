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
public class AuthenticationService {

    private final UserDetailsService userDetailsService;

    private  final PasswordEncoder passwordEncoder;

    public Authentication authenticate(String username, String password) {
        log.info("Starting authentication process for username: {}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(userDetails == null) {
            log.error("User not found for username: {}", username);
            throw new BadCredentialsException("Invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.error("Invalid password for username: {}", username);
            throw new BadCredentialsException("Invalid password");
        }

        log.info("Authentication successful for username: {}", username);

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    public Authentication authenticateWithEmail(String email, String password) {
        log.info("Starting authentication process for email: {}", email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        log.debug("User found for email: {}", email);

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.error("Invalid password for email: {}", email);
            throw new BadCredentialsException("Invalid password");
        }

        log.info("Authentication successful for email: {}", email);

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }
}
