package com.clinicapaw.backend_clinicapaw.presentation.controller;

import com.clinicapaw.backend_clinicapaw.presentation.advice.handler.SecurityErrorHandler;
import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthCreateUserAdminRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthLoginRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.AuthResponse;
import com.clinicapaw.backend_clinicapaw.service.auth.LoginService;
import com.clinicapaw.backend_clinicapaw.service.auth.RegisterUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final LoginService loginService;

    private final RegisterUserService registerUserService;

    @PostMapping("/login")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.loginService.login(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/sign-up")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserAdminRequest registerRequest) {
        return new ResponseEntity<>(registerUserService.createUserAdmin(registerRequest), HttpStatus.CREATED);
    }
}
