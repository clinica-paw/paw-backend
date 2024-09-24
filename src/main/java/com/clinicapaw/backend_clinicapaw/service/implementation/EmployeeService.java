package com.clinicapaw.backend_clinicapaw.service.implementation;

import com.clinicapaw.backend_clinicapaw.presentation.dto.EmployeeDTO;
import com.clinicapaw.backend_clinicapaw.service.interfaces.IEmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {

    @Override
    public EmployeeDTO getById(Long id) {
        return null;
    }

    @Override
    public List<EmployeeDTO> getAll() {
        return List.of();
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {

    }

    @Override
    public void update(Long id, EmployeeDTO employeeDTO) {

    }

    @Override
    public void delete(Long id) {

    }
}
