package com.clinicapaw.backend_clinicapaw.service.interfaces;

import com.clinicapaw.backend_clinicapaw.presentation.dto.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {

    EmployeeDTO getByDni(String dni);

    List<EmployeeDTO> getAll ();

    void save (EmployeeDTO employeeDTO);

    EmployeeDTO update (Long id, EmployeeDTO employeeDTO);

    void deleteByDni (String dni);
}
