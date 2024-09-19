package com.clinicapaw.backend_clinicapaw.service.auth;

import com.clinicapaw.backend_clinicapaw.persistence.model.UserEntity;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IUserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to load user by username: {}", username);

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() ->{
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with userName" + username);
                });

        log.info("User found with username: {}", username);

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        userEntity.getRoles()
                .forEach(role ->{
                    log.info("Adding role to authorities: ROLE_{}", role.getRoleEnum().name());
                    authorities
                            .add(new SimpleGrantedAuthority("ROLE_"
                                    .concat(role
                                            .getRoleEnum().name())));
                        });

        userEntity.getRoles().stream()
                .flatMap(role ->
                        role.getPermissions().stream())
                .forEach(permission ->{
                    log.info("Adding permission to authorities: {}", permission.getName());
                    authorities
                            .add(new SimpleGrantedAuthority(permission
                                    .getName()));
                        });

        log.info("User details loaded successfully for username: {}", username);

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEnabled(),
                userEntity.getAccountNonExpired(),
                userEntity.getCredentialsNonExpired(),
                userEntity.getAccountNonLocked(),
                authorities
        );
    }
}
