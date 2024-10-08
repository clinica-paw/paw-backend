package com.clinicapaw.backend_clinicapaw.persistence.model;

import com.clinicapaw.backend_clinicapaw.enums.RoleEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@JsonSerialize( as = Employee.class )
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private String dni;
    @Column(name = "first_name", nullable = false, length = 50, updatable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 70, updatable = false, unique = true)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;
    @Column(length = 100)
    private String direction;
    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Temporal(TemporalType.DATE)
    @Column(name="create_at", columnDefinition = "DATE")
    private LocalDate createAt;
    @PrePersist
    public void prePersist(){
        this.role = RoleEnum.EMPLOYEE;
        this.createAt = LocalDate.now();
    }

}
