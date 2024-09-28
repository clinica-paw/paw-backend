package com.clinicapaw.backend_clinicapaw.presentation.dto;

import com.clinicapaw.backend_clinicapaw.persistence.model.Customer;
import com.clinicapaw.backend_clinicapaw.persistence.model.Employee;
import com.clinicapaw.backend_clinicapaw.persistence.model.Pet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CustomerRecordDTO(@Valid Customer customer,
                                @Valid Pet pet,
                                @Valid Employee employee,
                                @NotBlank(message = "The description can't be null or empty")
                             @Size(max = 255, message = "Enter a maximum of 255 characters")
                             String description) {
}
