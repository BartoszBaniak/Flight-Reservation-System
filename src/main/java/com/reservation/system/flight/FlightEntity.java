package com.reservation.system.flight;

import com.reservation.system.airport.AirportEntity;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.dictionaries.flightType.FlightType;
import com.reservation.system.flightConnection.FlightConnectionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flights")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightId;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private AirportEntity flightDeparture;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private AirportEntity flightArrival;

    @Column(nullable = false)
    private LocalTime flightDepartureTime;

    @Column(nullable = false)
    private LocalTime flightArrivalTime;

    @Column(nullable = false)
    private String flightDuration;

    @Column(nullable = false)
    private LocalDate flightDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "connection_id", nullable = false)
    private FlightConnectionEntity flightConnection;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightType flightType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @Column(nullable = false)
    @Min(value = 112, message = "Flight must have at least 112 seats.")
    @Max(value = 189, message = "Flight must have at most 189 seats.")
    private int flightSeatsNumber;

}
