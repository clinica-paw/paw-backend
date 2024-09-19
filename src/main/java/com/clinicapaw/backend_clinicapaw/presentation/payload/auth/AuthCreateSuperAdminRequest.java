package com.clinicapaw.backend_clinicapaw.presentation.payload.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateSuperAdminRequest(@NotBlank String username,
                                          @NotBlank @Email String email,
                                          @NotBlank String password){
}
