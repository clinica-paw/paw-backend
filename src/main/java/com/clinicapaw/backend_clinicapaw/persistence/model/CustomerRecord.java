package com.clinicapaw.backend_clinicapaw.persistence.model;

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
@Entity
@Table(name = "records")
public class CustomerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToOne(targetEntity = Pet.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @OneToOne(targetEntity = Employee.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private String description;
    @Temporal(TemporalType.DATE)
    @Column(name="date", columnDefinition = "DATE")
    private LocalDate date;
    @PrePersist
    public void prePersist(){
        this.date = LocalDate.now();
    }
}
