package com.clinicapaw.backend_clinicapaw.configuration.security;

import com.clinicapaw.backend_clinicapaw.service.auth.UserDetailsServiceImpl;
import com.clinicapaw.backend_clinicapaw.util.jwt.JwtUtil;
import com.clinicapaw.backend_clinicapaw.util.jwt.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagement ->
                        httpSecuritySessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(httpRequests -> {
                    httpRequests
                            .requestMatchers(HttpMethod.POST, "/auth/sign-up-super-admin")
                            .permitAll();
                    httpRequests
                            .requestMatchers(HttpMethod.POST, "/auth/sign-in")
                            .hasAuthority("ROLE_SUPER_ADMIN");
                    httpRequests
                            .requestMatchers(HttpMethod.POST, "/auth/sign-in")
                            .hasAuthority("ROLE_ADMIN");
                    httpRequests
                            .requestMatchers(HttpMethod.POST, "/auth/sign-in")
                            .hasAuthority("ROLE_EMPLOYEE");
                    httpRequests
                            .anyRequest().denyAll();
                })
                .addFilterBefore(new JwtValidator(jwtUtil), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImpl);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
