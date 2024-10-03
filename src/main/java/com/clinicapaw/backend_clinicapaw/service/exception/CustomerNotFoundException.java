package com.clinicapaw.backend_clinicapaw.service.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Customer not found with dni:" + id);
    }
}
