package com.clinicapaw.backend_clinicapaw.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedIDException extends RuntimeException {

    public DuplicatedIDException(Long id) {
        super("The ID " + id + " already exists in the database.");
    }
}
