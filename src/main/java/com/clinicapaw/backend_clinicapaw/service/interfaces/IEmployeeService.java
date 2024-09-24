package com.clinicapaw.backend_clinicapaw.service.interfaces;

import com.clinicapaw.backend_clinicapaw.presentation.dto.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {

    EmployeeDTO getById (Long id);

    List<EmployeeDTO> getAll ();

    void save (EmployeeDTO employeeDTO);

    void update (Long id, EmployeeDTO employeeDTO);

    void delete (Long id);
}
