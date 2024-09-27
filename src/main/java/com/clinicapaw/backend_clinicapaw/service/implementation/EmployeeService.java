package com.clinicapaw.backend_clinicapaw.service.implementation;

import com.clinicapaw.backend_clinicapaw.persistence.model.Employee;
import com.clinicapaw.backend_clinicapaw.persistence.repository.IEmployeeRepository;
import com.clinicapaw.backend_clinicapaw.presentation.dto.EmployeeDTO;
import com.clinicapaw.backend_clinicapaw.service.exception.DuplicatedDniException;
import com.clinicapaw.backend_clinicapaw.service.exception.DuplicatedEmailException;
import com.clinicapaw.backend_clinicapaw.service.exception.EmployeeNotFoundException;
import com.clinicapaw.backend_clinicapaw.service.interfaces.IEmployeeService;
import com.clinicapaw.backend_clinicapaw.util.email.EmailContentMessage;
import com.clinicapaw.backend_clinicapaw.util.mapper.EmployeeMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    private final SendEmailService sendEmailService;

    @Transactional(readOnly = true)
    @Override
    public EmployeeDTO getByDni(String dni) {
        return employeeRepository.findByDni(dni)
                .map(EmployeeMapper::employeeToEmployeeDTO)
                .orElseThrow(() -> {
                    log.error("Employee not found with DNI: {}", dni);
                    return new EmployeeNotFoundException(dni);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDTO> getAll() {
        List<Employee> employeeList = employeeRepository.findAll();

        if (employeeList.isEmpty()) {
            log.warn("No employees found");
        }

        return employeeList.stream()
                .map(EmployeeMapper::employeeToEmployeeDTO)
                .toList();
    }

    @Transactional
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employeeSave = EmployeeMapper.employeeDTOToEmployee(employeeDTO);

        if(employeeRepository.findByDni(employeeSave.getDni()).isPresent()) {
            log.warn("DNI {} already exists in the database", employeeSave.getDni());
            throw new DuplicatedDniException(employeeSave.getDni());
        }

        if(employeeRepository.findByEmail(employeeSave.getEmail()).isPresent()) {
            log.warn("Email {} already exists in the database", employeeSave.getEmail());
            throw new DuplicatedEmailException(employeeSave.getEmail());
        }

        employeeRepository.save(employeeSave);
        log.info("Employee successfully saved: {}", employeeSave);

        String subject = EmailContentMessage.getWelcomeEmailSubject();
        String messageBody = EmailContentMessage.getWelcomeEmailMessage(employeeSave.getEmail());

        try {
            sendEmailService.sendEmail(employeeSave.getEmail(), subject, messageBody);
            log.info("Email sent to: {}", employeeSave.getEmail());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", employeeSave.getEmail(), e);
        }
    }

    @Transactional
    @Override
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFirstName(employeeDTO.firstName());
                    employee.setLastName(employeeDTO.lastName());
                    employee.setDni(employeeDTO.dni());
                    employee.setEmail(employeeDTO.email());
                    employee.setPhoneNumber(employeeDTO.phoneNumber());
                    employee.setDirection(employeeDTO.direction());
                    employee.setBirthDate(employeeDTO.birthDate());

                    employeeRepository.save(employee);
                    log.info("Employee successfully updated: {}", employee);

                    return EmployeeMapper.employeeToEmployeeDTO(employee);
                })
                .orElseThrow(() -> {
                    log.warn("Employee not found with id: {}", id);
                    return new EntityNotFoundException("Employee not found with id: " + id);
                });
    }

    @Transactional
    @Override
    public void delete(String dni) {
        Employee employee = employeeRepository.findByDni(dni)
                .orElseThrow(() -> {
                    log.error("Employee not found with DNI: {}", dni);
                    return new EmployeeNotFoundException(dni);
        });

        employeeRepository.delete(employee);
    }
}
