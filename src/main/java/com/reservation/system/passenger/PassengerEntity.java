package com.reservation.system.passenger;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passengers")
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int passengerId;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]{1,29}$",
            message = "Invalid first name. It should start with a capital letter and contain only letters.")
    private String firstName;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]{1,29}(-[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]{1,29})?$",
            message = "Invalid last name. It should start with a capital letter and may include a hyphenated second part.")
    private String lastName;

    @Column(nullable = false)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{9}", message = "Phone number must consist of exactly 9 digits.")
    private String phoneNumber;
}
