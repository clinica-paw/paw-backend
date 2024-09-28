package com.clinicapaw.backend_clinicapaw.service.auth;

import com.clinicapaw.backend_clinicapaw.enums.RoleEnum;
import com.clinicapaw.backend_clinicapaw.persistence.model.RoleEntity;
import com.clinicapaw.backend_clinicapaw.persistence.model.UserEntity;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IRoleRepository;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IUserEntityRepository;
import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthCreateUserAdminRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.AuthResponse;
import com.clinicapaw.backend_clinicapaw.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterUserService {

    private final IRoleRepository roleRepository;

    private final JwtUtil jwtUtil;

    private final IUserEntityRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse createUserAdmin(AuthCreateUserAdminRequest authCreateUserAdminRequest){
        log.info("Starting user admin creation with request: {}", authCreateUserAdminRequest);

        //extraemos el nombre de usuario
        String username = authCreateUserAdminRequest.username();
        //extramos el email
        String email = authCreateUserAdminRequest.email();
        //extraemos la contraseña
        String password = authCreateUserAdminRequest.password();
        //extramos la lista de rolesque se le haya dado
        List<String> roleRequest = authCreateUserAdminRequest.roleRequest().roleListName();
        log.debug("Extracted details - Username: {}, Email: {}", username, email);
        log.debug("Role request: {}", roleRequest);


        //ASIGNAR ROL
        //buscamos el ROL de ADMIN ingresado y lo almacenamos
        //si noi encuentra el role, crea el rol y lo guarda en la base de datos
        log.info("Starting to find or create the ADMIN role");
        RoleEntity userAdmin = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseGet(() -> {
                    log.warn("ADMIN role not found, creating a new one");

                    RoleEntity newRole = RoleEntity.builder()
                            .roleEnum(RoleEnum.ADMIN)
                            .build();

                    RoleEntity savedRole = roleRepository.save(newRole);
                    log.info("New ADMIN role created with ID: {}", savedRole.getId());

                    return savedRole;
                });

        //si el rol no es ADMIN se lanza una excepcion
        if (!RoleEnum.ADMIN.equals(userAdmin.getRoleEnum())) {
            log.warn("The entered role is not ADMIN.");
            throw new IllegalArgumentException("The role is not ADMIN.");
        }

        try {
            log.debug("Fetching roles for the provided role request.");
            //busca y traemos los roles de la base de datos
            Set<RoleEntity> roles = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest));
            log.debug("Roles found: {}", roles);

            log.debug("Final roles to be associated with the user: {}", roles);

            //si a ista esta vacia anza excepcion
            if (roles.isEmpty()) {
                log.error("No roles found for the requested list: {}", roleRequest);
                throw new NoSuchElementException("Roles not found for the list\n" + "\n: " + roleRequest);
            }

            log.debug("Final roles to be associated with the user: {}", roles);

            log.debug("Building UserEntity for the new admin user.");

            //crea e usuario
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
            log.info("UserEntity built for username: {}", username);

            log.debug("Saving the new user to the database.");
            //guarda el usuario
            UserEntity userCreated = userRepository.save(userEntity);
            log.info("User saved with ID: {}", userCreated.getId());

            //cada rol lo transformamos en una instancia de SimpleGrantedAthority, es decir cada rol es un permiso que puede ser otorgado a un usuario
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            log.debug("Converting roles to authorities.");
            userCreated.getRoles().forEach(roleEntity ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity
                                    .getRoleEnum().name())));
            log.debug("Roles converted to authorities: {}", authorities);

            //Optiene la lista de permisos y los añade tambien a SimpleGrantedAuthority
            log.debug("Adding permissions to authorities.");
            userCreated.getRoles().stream()
                    .flatMap(roleEntity ->
                            roleEntity.getPermissionsList().stream())
                    .forEach(permissionEntity ->
                            authorities.add(new SimpleGrantedAuthority(permissionEntity.getName())));
            log.debug("Permissions added to authorities");

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userCreated.getUsername(),
                    userCreated.getPassword(),
                    authorities);
            log.info("Authentication object created for user: {}", userCreated.getUsername());

            log.debug("Generating JWT token.");
            String accessToken = jwtUtil.generateToken(authentication);
            log.info("Access token generated for user: {}", userCreated.getUsername());

            return new AuthResponse(userCreated.getUsername(),
                    "User created successfully",
                    accessToken, true);

        } catch (Exception e) {
            log.error("Error creating user admin: {}", e.getMessage(), e);
            throw new RuntimeException("User creation failed", e);
        }
    }
}
