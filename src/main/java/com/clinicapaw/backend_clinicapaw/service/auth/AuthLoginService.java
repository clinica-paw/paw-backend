package com.clinicapaw.backend_clinicapaw.service.auth;

import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthLoginRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.AuthResponse;
import com.clinicapaw.backend_clinicapaw.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthLoginService {

    private final AuthenticationService authenticationService;

    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthLoginRequest request){
        String username = request.username();
        String password = request.password();

        log.info("Starting login process for username: {}", username);

        Authentication authentication = authenticationService.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Generating JWT for username: {}", username);

        String accessToken = jwtUtil.generateToken(authentication);

        log.info("Login process completed for username: {}", username);

        return AuthResponse.builder()
                .username(username)
                .message("User logged successfully")
                .jwt(accessToken)
                .status(true)
                .build();
    }
}
