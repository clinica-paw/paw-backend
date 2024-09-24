package com.clinicapaw.backend_clinicapaw.presentation.payload.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserAdminRequest(@NotBlank String username,
                                         @NotBlank String email,
                                         @NotBlank String password,
                                         @Valid AuthCreateRoleRequest roleRequest) {
}
