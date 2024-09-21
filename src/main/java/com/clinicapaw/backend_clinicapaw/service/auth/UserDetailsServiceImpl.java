package com.clinicapaw.backend_clinicapaw.service.auth;

import com.clinicapaw.backend_clinicapaw.persistence.model.UserEntity;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IUserEntityRepository;
import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthLoginRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.AuthResponse;
import com.clinicapaw.backend_clinicapaw.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserEntityRepository userRepository;

    private final JwtUtil jwtUtil;

    private  final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new UsernameNotFoundException("The user " + username + " does not exist.");
                });

        log.debug("User found: {}", userEntity.getUsername());

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> {
                    authorityList
                            .add(new SimpleGrantedAuthority("ROLE_" + role
                                    .getRoleEnum().name()));
                    log.debug("Assigned role to user: {}", role.getRoleEnum().name());
                });


        userEntity.getRoles().stream()
                .flatMap(role ->
                        role
                                .getPermissionsList().stream())
                .forEach(permission -> {
                    authorityList
                            .add(new SimpleGrantedAuthority(permission
                                    .getName()));
                    log.debug("Assigned permission to user: {}", permission.getName());
                });

        log.debug("User details loaded successfully for: {}", username);

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEnabled(),
                userEntity.getAccountNonExpired(),
                userEntity.getCredentialsNonExpired(),
                userEntity.getAccountNonLocked(),
                authorityList);
    }

    public Authentication authenticate(String username, String password) {
        log.debug("Authenticating user: {}", username);

        UserDetails userDetails = this.loadUserByUsername(username);

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

    public AuthResponse login(AuthLoginRequest request){
        String username = request.username();
        String password = request.password();
        log.info("Login attempt for user: {}", username);

        try {
            Authentication authentication = this.authenticate(username, password);
            log.info("User authenticated successfully: {}", username);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtil.generateToken(authentication);
            log.info("Access token generated for user: {}", username);

            return new AuthResponse(username, "User logged in successfully", accessToken, true);

        } catch (Exception e) {
            log.error("Login failed for user {}: {}", username, e.getMessage());
            return new AuthResponse(username,
                    "Login failed",
                    null,
                    false);
        }
    }
}
