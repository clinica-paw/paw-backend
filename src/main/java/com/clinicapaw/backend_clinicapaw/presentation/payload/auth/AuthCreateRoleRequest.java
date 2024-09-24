package com.clinicapaw.backend_clinicapaw.presentation.payload.auth;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthCreateRoleRequest(@Size(max=3, message = "the user cannot had more than 3 role")
                                    List<String> roleListName) {
}
