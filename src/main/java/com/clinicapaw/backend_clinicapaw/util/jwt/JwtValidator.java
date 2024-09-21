package com.clinicapaw.backend_clinicapaw.util.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidator extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Received request at: {}", request.getRequestURI());

        if (jwtToken != null) {
            log.debug("JWT Token received: {}", jwtToken);

            jwtToken = jwtToken.substring(7);
            log.debug("Extracted JWT Token (without 'Bearer'): {}", jwtToken);

            try {
                DecodedJWT decodedJWT = jwtUtil.verifyToken(jwtToken);
                log.debug("JWT verified successfully");

                String username = jwtUtil.extractUsername(decodedJWT);
                log.debug("Extracted Username from JWT: {}", username);

                String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();
                log.debug("Authorities extracted from JWT: {}", stringAuthorities);

                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
                log.debug("Authentication successfully set for user: {}", username);

            } catch (Exception e) {
                log.error("Error verifying JWT token: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is invalid");
            }

        }else {
            log.warn("Authorization header is missing or invalid");
        }
        filterChain.doFilter(request, response);
    }
}
