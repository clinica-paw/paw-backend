package com.clinicapaw.backend_clinicapaw.service.interfaces;

import com.clinicapaw.backend_clinicapaw.presentation.dto.CustomerDTO;

import java.util.List;

public interface ICustomerService {

    CustomerDTO getById(Long id);

    List<CustomerDTO> getAll ();

    void save (CustomerDTO customerDTO);

    CustomerDTO update (Long id, CustomerDTO customerDTO);

    void deleteById (Long id);
}
