package com.clinicapaw.backend_clinicapaw.presentation.dto;

import com.clinicapaw.backend_clinicapaw.persistence.model.GenderEntity;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record PetDTO(Long id,
                     @NotBlank(message = "The name can´t be null or empty")
                     @Size(min = 2, max = 50, message = "Enter a minimum of 2 characters and a maximum of 50")
                     String firstName,
                     @NotBlank(message = "The lastName can´t be null or empty")
                     @Size(min = 2, max = 50, message = "Enter a minimum of 2 characters and a maximum of 50")
                     String lastName,
                     @NotBlank(message = "The specie can't be null or empty")
                     @Size(min = 3, max = 50, message = "Enter a minimum of 3 characters and a maximum of 50")
                     String specie,
                     @NotNull(message = "Gender is required")
                     GenderEntity gender,
                     @NotBlank(message = "The breed can't be null or empty")
                     @Size(min = 3, max = 50, message = "Enter a minimum of 3 characters and a maximum of 50")
                     String breed,
                     @NotNull(message = "Age can't be null")
                     @Min(value = 0, message = "Age must be at least 0")
                     Integer age,
                     @NotNull(message = "Weight can't be null")
                     @DecimalMin(value = "0.1", message = "Weight must be at least 0.1")
                     Double weight,
                     @NotBlank(message = "The description can't be null or empty")
                     @Size(max = 255, message = "Enter a maximum of 255 characters")
                     String description) {
}
