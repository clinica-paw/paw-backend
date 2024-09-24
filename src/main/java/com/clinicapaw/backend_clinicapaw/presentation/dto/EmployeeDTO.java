package com.clinicapaw.backend_clinicapaw.presentation.dto;

public record EmployeeDTO(Long id,
                          String dni,
                          String firstName,
                          String lastName,
                          String email,
                          String phoneNumber,
                          String direction) {
}
