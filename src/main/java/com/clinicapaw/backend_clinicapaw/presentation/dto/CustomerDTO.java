package com.clinicapaw.backend_clinicapaw.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CustomerDTO(Long id,
                          @NotBlank(message = "The name can´t be null or empty")
                          @Size(min = 2, max = 50, message = "Enter a minimum of 2 characters and a maximum of 50")
                          String firstName,
                          @NotBlank(message = "The lastName can´t be null or empty")
                          @Size(min = 2, max = 50, message = "Enter a minimum of 2 characters and a maximum of 50")
                          String lastName,
                          @NotBlank(message = "The phone number can´t be null or empty")
                          @Size(min = 7, max = 20, message = "Enter a minimum of 2 characters and a maximum of 20")
                          String phoneNumber,
                          @Valid
                          PetDTO petDTO) {
}
