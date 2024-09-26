package com.clinicapaw.backend_clinicapaw.persistence.repository;

import com.clinicapaw.backend_clinicapaw.persistence.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT c FROM Employee c WHERE c.dni = :dni")
    Optional<Employee> findByDni(@Param("dni")String dni);

    @Query("SELECT c FROM Employee c WHERE c.email = :email")
    Optional<Employee> findByEmail(@Param("email")String email);
}
