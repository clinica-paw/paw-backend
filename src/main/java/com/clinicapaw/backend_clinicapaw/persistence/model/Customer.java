package com.clinicapaw.backend_clinicapaw.persistence.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50, updatable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 70, updatable = false, unique = true)
    private String lastName;

    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Pet.class)
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
