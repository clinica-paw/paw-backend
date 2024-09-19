package com.clinicapaw.backend_clinicapaw.presentation.payload.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username,
                               @NotBlank String password){
}
