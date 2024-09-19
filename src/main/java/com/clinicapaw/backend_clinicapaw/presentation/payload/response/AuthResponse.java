package com.clinicapaw.backend_clinicapaw.presentation.payload.response;

import lombok.Builder;

@Builder
public record AuthResponse(String username,
                           String message,
                           String jwt,
                           boolean status){
}
