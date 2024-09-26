package com.clinicapaw.backend_clinicapaw.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException(String email) {
        super("The email " + email + " already exists in the database.");
    }
}
