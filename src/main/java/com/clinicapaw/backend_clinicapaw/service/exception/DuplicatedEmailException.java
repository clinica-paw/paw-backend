package com.clinicapaw.backend_clinicapaw.service.exception;

public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException(String email) {
        super("The email " + email + " already exists in the database.");
    }
}
