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
public class LoginService {

    private final JwtUtil jwtUtil;

    private final AuthenticateService authenticateService;

    public AuthResponse login(AuthLoginRequest request){
        String username = request.username();
        String password = request.password();
        log.info("Login attempt for user: {}", username);

        try {
            Authentication authentication = authenticateService.authenticate(username, password);
            log.info("User authenticated successfully: {}", username);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtil.generateToken(authentication);
            log.info("Access token generated for user: {}", username);

            return AuthResponse.builder()
                    .username(username)
                    .message("User logged in successfully")
                    .jwt(accessToken)
                    .status(true)
                    .build();

        } catch (Exception e) {
            log.error("Login failed for user {}: {}", username, e.getMessage());
            return AuthResponse.builder()
                    .username(username)
                    .message("Login failed")
                    .jwt(null)
                    .status(false)
                    .build();
        }
    }
}
