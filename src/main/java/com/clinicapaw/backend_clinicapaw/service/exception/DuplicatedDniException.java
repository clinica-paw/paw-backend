package com.clinicapaw.backend_clinicapaw.service.exception;

public class DuplicatedDniException extends RuntimeException {

    public DuplicatedDniException(String dni) {
        super("The DNI " + dni + " already exists in the database.");
    }
}
