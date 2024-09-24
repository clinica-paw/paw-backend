package com.clinicapaw.backend_clinicapaw.persistence.repository;

import com.clinicapaw.backend_clinicapaw.persistence.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
}
