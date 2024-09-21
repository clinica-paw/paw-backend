package com.clinicapaw.backend_clinicapaw;

import com.clinicapaw.backend_clinicapaw.configuration.security.SecurityConfig;
import com.clinicapaw.backend_clinicapaw.enums.RoleEnum;
import com.clinicapaw.backend_clinicapaw.persistence.model.RoleEntity;
import com.clinicapaw.backend_clinicapaw.persistence.model.UserEntity;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IRoleRepository;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IUserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
public class BackendClinicapawApplication {

	private final SecurityConfig securityConfig;

	private final IUserEntityRepository userRepository;

	private final IRoleRepository roleRepository;

	public BackendClinicapawApplication(SecurityConfig securityConfig, IUserEntityRepository userRepository, IRoleRepository roleRepository) {
		this.securityConfig = securityConfig;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendClinicapawApplication.class, args);
	}

	@Bean
	public CommandLineRunner init() {
		return args -> {
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.build();
			roleRepository.save(roleAdmin);

			UserEntity user = UserEntity.builder()
					.id(1L)
					.username("Israel")
					.email("israel.bastion123@gmail.com")
					.password(securityConfig.passwordEncoder().encode("password123"))
					.enabled(true)
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.roles(Set.of(roleAdmin))
					.createAt(LocalDate.now())
					.build();
			userRepository.save(user);
		};
	}
}
