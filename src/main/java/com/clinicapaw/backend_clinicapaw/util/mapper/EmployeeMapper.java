package com.clinicapaw.backend_clinicapaw.util.mapper;

import com.clinicapaw.backend_clinicapaw.persistence.model.Employee;
import com.clinicapaw.backend_clinicapaw.presentation.dto.EmployeeDTO;

public class EmployeeMapper {

    public static EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dni(employee.getDni())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .direction(employee.getDirection())
                .birthDate(employee.getBirthDate())
                .build();
    }

    public static Employee employeeDTOToEmployee(EmployeeDTO employeeDTO) {
        return Employee.builder()
                .id(employeeDTO.id())
                .firstName(employeeDTO.firstName())
                .lastName(employeeDTO.lastName())
                .dni(employeeDTO.dni())
                .email(employeeDTO.email())
                .phoneNumber(employeeDTO.phoneNumber())
                .direction(employeeDTO.direction())
                .birthDate(employeeDTO.birthDate())
                .build();
    }
}
