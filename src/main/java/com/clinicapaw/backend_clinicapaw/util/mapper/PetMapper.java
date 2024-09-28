package com.clinicapaw.backend_clinicapaw.util.mapper;

import com.clinicapaw.backend_clinicapaw.persistence.model.Pet;
import com.clinicapaw.backend_clinicapaw.presentation.dto.PetDTO;

public class PetMapper {

    public static PetDTO petToPetDTO(Pet pet){
        return PetDTO.builder()
                .id(pet.getId())
                .firstName(pet.getFirstName())
                .lastName(pet.getLastName())
                .specie(pet.getSpecie())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .weight(pet.getWeight())
                .description(pet.getDescription())
                .gender(pet.getGender())
                .build();
    }

    public static Pet petDTOToPet(PetDTO petDTO){
        return Pet.builder()
                .id(petDTO.id())
                .firstName(petDTO.firstName())
                .lastName(petDTO.lastName())
                .specie(petDTO.specie())
                .breed(petDTO.breed())
                .age(petDTO.age())
                .weight(petDTO.weight())
                .description(petDTO.description())
                .gender(petDTO.gender())
                .build();
    }
}
