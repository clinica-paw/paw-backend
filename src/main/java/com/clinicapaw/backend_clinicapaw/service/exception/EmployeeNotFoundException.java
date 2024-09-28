package com.clinicapaw.backend_clinicapaw.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String dni) {
        super("Employee not found with dni:" + dni);
    }

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with ID:" + id);
    }
}
