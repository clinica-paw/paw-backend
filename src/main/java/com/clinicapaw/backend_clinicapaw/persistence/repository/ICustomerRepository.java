package com.clinicapaw.backend_clinicapaw.persistence.repository;

import com.clinicapaw.backend_clinicapaw.persistence.model.Customer;
import com.clinicapaw.backend_clinicapaw.persistence.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
}
