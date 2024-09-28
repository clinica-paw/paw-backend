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
import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;
import org.springframework.security.config.Customizer;
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
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagement ->
                        httpSecuritySessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.POST, "/auth/**")
                            .permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/v1/employee")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/employee")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET , "/api/v1/employee/{dni}").
                            hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.GET , "/api/v1/employees")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/v1/employee/{dni}")
                            .hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/api/v1/customer")
                            .hasRole("EMPLOYEE");
                    http.requestMatchers(HttpMethod.PUT, "/api/v1/customer")
                            .hasRole("EMPLOYEE");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/customer/{id}")
                            .hasRole("EMPLOYEE");
                    http.requestMatchers(HttpMethod.GET, "/api/v1/customers")
                            .hasRole("EMPLOYEE");
                    http.requestMatchers(HttpMethod.DELETE, "/api/v1/customer/{id}")
                            .hasRole("EMPLOYEE");
                    http.anyRequest()
                            .denyAll();
                })
                .addFilterBefore(new JwtValidator(this.jwtUtil), BasicAuthenticationFilter.class)
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

    public Customizer<AuthorizationAdvisorProxyFactory> skipValueTypes() {
        return (factory) -> {
            factory.setTargetVisitor(AuthorizationAdvisorProxyFactory.TargetVisitor.defaultsSkipValueTypes());
        };
    }
}
