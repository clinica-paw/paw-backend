package com.clinicapaw.backend_clinicapaw.service.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String dni) {
        super("Employee not found with dni:" + dni);
    }

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with ID:" + id);
    }
}
