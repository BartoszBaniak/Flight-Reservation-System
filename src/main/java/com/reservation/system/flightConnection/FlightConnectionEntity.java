package com.reservation.system.flightConnection;

import com.reservation.system.airport.AirportEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_connections")
public class FlightConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private AirportEntity departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private AirportEntity arrivalAirport;

    @Column(nullable = false)
    private String flightNumber;
}
