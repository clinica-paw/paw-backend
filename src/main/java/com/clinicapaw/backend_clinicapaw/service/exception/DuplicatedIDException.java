package com.clinicapaw.backend_clinicapaw.service.exception;

public class DuplicatedIDException extends RuntimeException {

    public DuplicatedIDException(Long id) {
        super("The ID " + id + " already exists in the database.");
    }
}
