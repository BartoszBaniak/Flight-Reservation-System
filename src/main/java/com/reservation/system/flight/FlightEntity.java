package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.dictionaries.flightType.FlightType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalTime flightTime; //hour:minute

    @Column(nullable = false)
    private LocalDate flightDate;

    @Column(nullable = false)
    private FlightNumber flightNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightType flightType;

    @Column(nullable = false)
    private int flightSeatsNumber;

    public FlightEntity(Airport flightDeparture, Airport flightArrival, LocalTime flightTime, LocalDate flightDate, FlightNumber flightNumber, FlightType flightType, int flightSeatsNumber) {
        this.flightDeparture = flightDeparture;
        this.flightArrival = flightArrival;
        this.flightTime = flightTime;
        this.flightDate = flightDate;
        this.flightNumber = flightNumber;
        this.flightType = flightType;
        this.flightSeatsNumber = flightSeatsNumber;
    }

    public FlightEntity() {

    }


}
