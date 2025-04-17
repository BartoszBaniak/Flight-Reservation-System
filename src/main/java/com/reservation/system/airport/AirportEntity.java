package com.reservation.system.airport;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
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
