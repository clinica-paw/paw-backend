package com.clinicapaw.backend_clinicapaw.presentation.controller;

import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthCreateSuperAdminRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.auth.AuthLoginRequest;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.AuthResponse;
import com.clinicapaw.backend_clinicapaw.service.auth.AuthLoginService;
import com.clinicapaw.backend_clinicapaw.service.auth.AuthRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private  final AuthRegisterService authRegisterService;

    private final AuthLoginService authLoginService;

    @PostMapping("/sign-up-super-admin")
    public ResponseEntity<AuthResponse> registerSuperAdmin(@RequestBody @Valid AuthCreateSuperAdminRequest registerRequest) {
        return new ResponseEntity<>(this.authRegisterService.createSuperAdmin(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.authLoginService.login(userRequest), HttpStatus.OK);
    }
}
