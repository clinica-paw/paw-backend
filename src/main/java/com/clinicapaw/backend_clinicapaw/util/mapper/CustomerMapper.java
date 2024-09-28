package com.clinicapaw.backend_clinicapaw.util.mapper;

import com.clinicapaw.backend_clinicapaw.persistence.model.Customer;
import com.clinicapaw.backend_clinicapaw.persistence.model.Pet;
import com.clinicapaw.backend_clinicapaw.presentation.dto.CustomerDTO;
import com.clinicapaw.backend_clinicapaw.presentation.dto.PetDTO;

public class CustomerMapper {

    public static CustomerDTO customerToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .petDTO(PetDTO.builder()
                        .id(customer.getPet().getId())
                        .age(customer.getPet().getAge())
                        .breed(customer.getPet().getBreed())
                        .description(customer.getPet().getDescription())
                        .gender(customer.getPet().getGender())
                        .weight(customer.getPet().getWeight())
                        .specie(customer.getPet().getSpecie())
                        .build())
                .build();
    }

    public static Customer customerDTOToCustomer(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.id())
                .firstName(customerDTO.firstName())
                .lastName(customerDTO.firstName())
                .phoneNumber(customerDTO.phoneNumber())
                .pet(Pet.builder()
                        .id(customerDTO.petDTO().id())
                        .age(customerDTO.petDTO().age())
                        .breed(customerDTO.petDTO().breed())
                        .description(customerDTO.petDTO().description())
                        .gender(customerDTO.petDTO().gender())
                        .weight(customerDTO.petDTO().weight())
                        .specie(customerDTO.petDTO().specie())
                        .build())
                .build();
    }

}
