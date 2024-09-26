package com.clinicapaw.backend_clinicapaw.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedDniException extends RuntimeException {

    public DuplicatedDniException(String dni) {
        super("The DNI " + dni + " already exists in the database.");
    }
}
