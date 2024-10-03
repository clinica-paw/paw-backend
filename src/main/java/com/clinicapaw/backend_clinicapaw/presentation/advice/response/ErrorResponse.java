package com.clinicapaw.backend_clinicapaw.presentation.advice.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse (HttpStatus httpStatus,
                             String message) {

}
