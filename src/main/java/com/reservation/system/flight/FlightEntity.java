package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.dictionaries.flightType.FlightType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "flights")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Airport departure;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Airport arrival;

    @Column(nullable = false)
    private LocalTime flightTime; //hour:minute

    @Column(nullable = false)
    private LocalDate flightDate;

    @Column(nullable = false)
    private FlightNumber flightNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightType flightType;

    @Column(nullable = false)
    private int seatNumber;
}
