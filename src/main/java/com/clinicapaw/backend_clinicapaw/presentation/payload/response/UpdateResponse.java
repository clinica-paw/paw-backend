package com.clinicapaw.backend_clinicapaw.presentation.payload.response;

import lombok.Builder;

@Builder
public record UpdateResponse(String message,
                             Object object) {
}
