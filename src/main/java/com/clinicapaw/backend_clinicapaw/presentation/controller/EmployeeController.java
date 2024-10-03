package com.clinicapaw.backend_clinicapaw.presentation.controller;

import com.clinicapaw.backend_clinicapaw.presentation.advice.handler.SecurityErrorHandler;
import com.clinicapaw.backend_clinicapaw.presentation.dto.EmployeeDTO;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.CreateResponse;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.DeleteResponse;
import com.clinicapaw.backend_clinicapaw.presentation.payload.response.UpdateResponse;
import com.clinicapaw.backend_clinicapaw.service.exception.DuplicatedDniException;
import com.clinicapaw.backend_clinicapaw.service.exception.DuplicatedEmailException;
import com.clinicapaw.backend_clinicapaw.service.exception.EmployeeNotFoundException;
import com.clinicapaw.backend_clinicapaw.service.implementation.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
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
@Tag(name="Employee Controller", description = "Controller for employees crud")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employee/{dni}")
    @Operation(
            summary = "Get a employee",
            description = "Return a employee by DNI",
            tags = {"Employee Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Has no data.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmployeeDTO.class)
                            )
                    )
            }
    )
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    @AuthorizeReturnObject
    public ResponseEntity<EmployeeDTO> get(@PathVariable("dni") String dni){
        log.info("Request received to get employee with DNI: {}", dni);

        try{
            EmployeeDTO employeeDTO = employeeService.getByDni(dni);
            log.info("Employee found: {}", employeeDTO);
            return ResponseEntity.ok(employeeDTO);

        }catch (EntityNotFoundException entityNotFoundException){
            log.warn("Employee not found with DNI: {}", dni);
            throw entityNotFoundException;

        }catch (Exception exception){
            log.error("An unexpected error occurred while retrieving employee with DNI: {}", dni, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employees")
    @Operation(
            summary = "Get all employees",
            description = "Return all employees from the data base in a format json list.",
            tags = {"Employee Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Has no data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmployeeDTO.class)
                            )
                    )
            }
    )
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    @AuthorizeReturnObject
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        log.info("Fetching all employees from the database");
        List<EmployeeDTO> employeeDTOList = employeeService.getAll();

        if (employeeDTOList.isEmpty()) {
            log.warn("No employees found");
            return ResponseEntity.noContent().build();
        }

        log.info("Found {} employees", employeeDTOList.size());
        return ResponseEntity.ok(employeeDTOList);
    }

    @PostMapping("/employee")
    @Operation(
            summary = "Create a employee",
            description = "Receives data from employee in json format to stores it in the data base and send confirm message in the body.",
            tags = {"Employee Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data from employee in format json.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmployeeDTO.class)
                            )
                    )
            }
    )
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<?> create(@RequestBody @Valid EmployeeDTO employeeDTO){
        log.info("Creating employee: {}", employeeDTO);

        try {
            employeeService.save(employeeDTO);

            CreateResponse response = CreateResponse.builder()
                    .message("¡Creado correctamente!")
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DuplicatedDniException | DuplicatedEmailException exception) {
            log.warn("Error creating employee: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        }
    }

    @PutMapping("/employee")
    @Operation(
            summary = "Update a employee",
            description = "Find a employee by ID to updates her data in the data base and send confirm message in the body.",
            tags = {"Employee Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New data from employee in format json for the update process.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmployeeDTO.class)
                            )
                    )
            }
    )
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<?> update(@RequestBody @Valid EmployeeDTO employeeDTO){
        log.info("Updating employee with id {}: {}", employeeDTO.id(), employeeDTO);
        EmployeeDTO updatedEmployeeDTO = employeeService.update(employeeDTO.id(), employeeDTO);

        UpdateResponse response = UpdateResponse.builder()
                .message("¡Actualizado correctamente!")
                .object(updatedEmployeeDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/employee/{dni}")
    @Operation(
            summary = "Delete a employee",
            description = "Finds the employee by DNI to deletes it and send confirm message in the body.",
            tags = {"Employee Controller"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Has no data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmployeeDTO.class)
                            )
                    )
            }
    )
    @HandleAuthorizationDenied(handlerClass = SecurityErrorHandler.class)
    public ResponseEntity<?> delete(@PathVariable("dni") String dni){
        log.info("Deleting employee with DNI: {}", dni);

        try {
            employeeService.deleteByDni(dni);

            DeleteResponse response = DeleteResponse.builder()
                    .message("¡Eliminado correctamente!")
                    .build();

            return ResponseEntity.ok(response);

        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.warn("Attempted to delete non-existing employee: {}", employeeNotFoundException.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeNotFoundException.getMessage());

        } catch (Exception ex) {
            log.error("Error occurred while deleting employee: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }

}
