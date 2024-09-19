package com.clinicapaw.backend_clinicapaw.service.auth;

import com.clinicapaw.backend_clinicapaw.enums.RoleEnum;
import com.clinicapaw.backend_clinicapaw.persistence.model.RoleEntity;
import com.clinicapaw.backend_clinicapaw.persistence.model.UserEntity;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IRoleRepository;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IUserEntityRepository;
import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthCreateSuperAdminRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.AuthResponse;
import com.clinicapaw.backend_clinicapaw.service.implementation.SendEmailService;
import com.clinicapaw.backend_clinicapaw.util.email.EmailContentMessage;
import com.clinicapaw.backend_clinicapaw.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthRegisterService {

    private final IUserEntityRepository userRepository;

    private  final PasswordEncoder passwordEncoder;

    private  final JwtUtil jwtUtil;

    private  final IRoleRepository roleRepository;

    private final SendEmailService sendEmailService;

    public AuthResponse createSuperAdmin(AuthCreateSuperAdminRequest authCreateSuperAdminRequest) {
        String username = authCreateSuperAdminRequest.username();
        String email = authCreateSuperAdminRequest.email();
        String password = authCreateSuperAdminRequest.password();

        log.info("Starting the process to create super admin");

        List<String> roleRequest = new ArrayList<>();
        roleRequest.add(RoleEnum.SUPER_ADMIN.toString());
        log.info("Assigning SUPER_ADMIN role to the first user");

        Set<RoleEntity> roles = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest));
        roleRepository.saveAll(roles);

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        log.info("Built UserEntity for the user: {}", username);

        UserEntity userCreated = userRepository.save(userEntity);
        log.info("User {} saved successfully", userCreated.getUsername());

        log.info("Assigning roles to the created user: {}", userCreated.getUsername());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        userCreated.getRoles().forEach(roleEntity ->
                authorities
                        .add(new SimpleGrantedAuthority("ROLE_" + roleEntity
                                .getRoleEnum().name()))
        );

        log.info("Assigning permissions to the created user: {}", userCreated.getUsername());

        userCreated.getRoles().stream()
                .flatMap(roleEntity ->
                        roleEntity
                                .getPermissions().stream())
                .forEach(permissionEntity ->
                        authorities
                                .add(new SimpleGrantedAuthority(permissionEntity
                                        .getName()))
                );

        log.info("Authenticating the user: {}", userCreated.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userCreated.getUsername(),
                userCreated.getPassword(),
                authorities
        );

        String accessToken = jwtUtil.generateToken(authentication);
        log.info("Token generated for the user: {}", userCreated.getUsername());

        String subject = EmailContentMessage.getWelcomeEmailSubject();
        String messageBody = EmailContentMessage.getWelcomeEmailMessage(email);
        sendEmailService.sendEmail(email, subject, messageBody);

        log.info("Admin created successfully: {}", userCreated.getEmail());
        return new AuthResponse(userCreated.getUsername(), "User created successfully", accessToken, true);
    }
}
