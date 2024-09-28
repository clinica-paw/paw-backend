package com.clinicapaw.backend_clinicapaw.presentation.controller;

import com.clinicapaw.backend_clinicapaw.presentation.advice.handler.SecurityErrorHandler;
import com.clinicapaw.backend_clinicapaw.presentation.dto.CustomerDTO;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.CreateResponse;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.DeleteResponse;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.UpdateResponse;
import com.clinicapaw.backend_clinicapaw.service.exception.*;
import com.clinicapaw.backend_clinicapaw.service.implementation.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
@Tag(name="Customer Controller", description = "Controller for customers crud")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customer/{id}")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    @AuthorizeReturnObject
    public ResponseEntity<CustomerDTO> get(@PathVariable("id") Long id){
        log.info("Request received to get customer with ID: {}", id);

        try{
            CustomerDTO customerDTO = customerService.getById(id);
            log.info("Customer found: {}", customerDTO);
            return ResponseEntity.ok(customerDTO);

        }catch (CustomerNotFoundException customerNotFoundException){
            log.warn("Customer not found with ID: {}", id);
            throw customerNotFoundException;

        }catch (Exception exception){
            log.error("An unexpected error occurred while retrieving customer with ID: {}", id, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/customers")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    @AuthorizeReturnObject
    public ResponseEntity<List<CustomerDTO>> getAll() {
        log.info("Fetching all customers");
        List<CustomerDTO> customerDTOList = customerService.getAll();

        if (customerDTOList.isEmpty()) {
            log.warn("No customers found");
            return ResponseEntity.noContent().build();
        }

        log.info("Found {} customers", customerDTOList.size());
        return ResponseEntity.ok(customerDTOList);
    }

    @PostMapping("/customer")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<?> create(@RequestBody @Valid CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO);

        try {
            customerService.save(customerDTO);

            CreateResponse response = CreateResponse.builder()
                    .message("¡Creado correctamente!")
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DuplicatedIDException duplicatedIDException) {
            log.warn("Error creating customer: {}", duplicatedIDException.getMessage());
            throw duplicatedIDException;
        }
    }

    @PutMapping("/customer")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<?> update(@RequestBody @Valid CustomerDTO customerDTO){
        log.info("Updating customer with id {}: {}", customerDTO.id(), customerDTO);
        CustomerDTO updatedCustomerDTO = customerService.update(customerDTO.id(), customerDTO);

        UpdateResponse response = UpdateResponse.builder()
                .message("¡Actualizado correctamente!")
                .object(updatedCustomerDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/customer/{dni}")
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<?> delete(@PathVariable("dni") Long id){
        log.info("Deleting customer with ID: {}", id);

        try {
            customerService.deleteById(id);

            DeleteResponse response = DeleteResponse.builder()
                    .message("¡Eliminado correctamente!")
                    .build();

            return ResponseEntity.ok(response);

        } catch (CustomerNotFoundException customerNotFoundException) {
            log.warn("Attempted to delete non-existing employee: {}", customerNotFoundException.getMessage());
            throw customerNotFoundException;

        } catch (Exception ex) {
            log.error("Error occurred while deleting customer: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }
}
