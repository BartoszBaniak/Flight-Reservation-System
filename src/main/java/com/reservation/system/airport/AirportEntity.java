package com.reservation.system.airport;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "airports")
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int airportId;

    @Column(nullable = false, unique = true)
    private String airportCode;

    @Column(nullable = false)
    private String airportCity;
}
