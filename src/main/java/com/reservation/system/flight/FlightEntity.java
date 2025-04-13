package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.dictionaries.flightType.FlightType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
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

    public FlightEntity(
            Airport flightDeparture,
            Airport flightArrival,
            LocalTime flightDepartureTime,
            LocalTime flightArrivalTime,
            String flightDuration,
            LocalDate flightDate,
            FlightNumber flightNumber,
            FlightType flightType,
            int flightSeatsNumber) {

        this.flightDeparture = flightDeparture;
        this.flightArrival = flightArrival;
        this.flightDepartureTime = flightDepartureTime;
        this.flightArrivalTime = flightArrivalTime;
        this.flightDuration = flightDuration;
        this.flightDate = flightDate;
        this.flightNumber = flightNumber;
        this.flightType = flightType;
        this.flightStatus = FlightStatus.SCHEDULED;
        this.flightSeatsNumber = flightSeatsNumber;
    }

    public FlightEntity() {

    }


}
