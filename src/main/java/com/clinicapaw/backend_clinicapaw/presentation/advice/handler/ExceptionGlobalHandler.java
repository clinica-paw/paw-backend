package com.clinicapaw.backend_clinicapaw.presentation.advice.handler;

import com.clinicapaw.backend_clinicapaw.presentation.advice.response.ErrorResponse;
import com.clinicapaw.backend_clinicapaw.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(DuplicatedDniException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedDniException(DuplicatedDniException ex) {
        return  ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedEmailException(DuplicatedEmailException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(DuplicatedIDException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedIDException(DuplicatedIDException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }
}
