package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.dictionaries.flightType.FlightType;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Airport flightDeparture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Airport flightArrival;

    @Column(nullable = false)
    private LocalTime flightDepartureTime; //hour:minute

    @Column(nullable = false)
    private LocalTime flightArrivalTime; //hour:minute

    @Column(nullable = false)
    private String flightDuration; //hour:minute

    @Column(nullable = false)
    private LocalDate flightDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightNumber flightNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightType flightType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;

    @Column(nullable = false)
    private int flightSeatsNumber;

}
