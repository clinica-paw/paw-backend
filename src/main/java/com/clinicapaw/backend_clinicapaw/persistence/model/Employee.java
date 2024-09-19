package com.clinicapaw.backend_clinicapaw.persistence.model;

import com.clinicapaw.backend_clinicapaw.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
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

    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.EMPLOYEE ;

}
