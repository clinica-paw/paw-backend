package com.clinicapaw.backend_clinicapaw.service.implementation;

import com.clinicapaw.backend_clinicapaw.persistence.model.Customer;
import com.clinicapaw.backend_clinicapaw.persistence.repository.ICustomerRepository;
import com.clinicapaw.backend_clinicapaw.presentation.dto.CustomerDTO;
import com.clinicapaw.backend_clinicapaw.service.exception.*;
import com.clinicapaw.backend_clinicapaw.service.interfaces.ICustomerService;
import com.clinicapaw.backend_clinicapaw.util.mapper.CustomerMapper;
import com.clinicapaw.backend_clinicapaw.util.mapper.PetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @Override
    public CustomerDTO getById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::customerToCustomerDTO)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new CustomerNotFoundException(id);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerDTO> getAll() {
        List<Customer> customersList = customerRepository.findAll();

        if (customersList.isEmpty()) {
            log.warn("No employees found");
        }

        return customersList.stream()
                .map(CustomerMapper::customerToCustomerDTO)
                .toList();
    }

    @Transactional
    @Override
    public void save(CustomerDTO customerDTO) {
        Customer customerSave = CustomerMapper.customerDTOToCustomer(customerDTO);

        if(customerRepository.findById(customerSave.getId()).isPresent()) {
            log.warn("ID {} already exists in the database", customerSave.getId());
            throw new DuplicatedIDException(customerSave.getId());
        }

        customerRepository.save(customerSave);
        log.info("Customer successfully saved: {}", customerSave);
    }

    @Transactional
    @Override
    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDTO.firstName());
                    customer.setLastName(customerDTO.lastName());
                    customer.setPhoneNumber(customerDTO.phoneNumber());
                    customer.setPet(PetMapper.petDTOToPet(customerDTO.petDTO()));

                    customerRepository.save(customer);
                    log.info("Customer successfully updated: {}", customer);

                    return CustomerMapper.customerToCustomerDTO(customer);
                })
                .orElseThrow(() -> {
                    log.warn("Customer not found with id: {}", id);
                    return new CustomerNotFoundException(id);
                });
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with DNI: {}", id);
                    return new CustomerNotFoundException(id);
                });

        customerRepository.delete(customer);
    }
}
