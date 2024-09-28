package com.clinicapaw.backend_clinicapaw.presentation.advice.handler;

import com.clinicapaw.backend_clinicapaw.service.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(DuplicatedDniException.class)
    public String handleDuplicatedDniException(DuplicatedDniException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public String handleDuplicatedEmailException(DuplicatedEmailException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public String handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public String handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicatedIDException.class)
    public String handleDuplicatedIDException(DuplicatedIDException ex) {
        return ex.getMessage();
    }
}
