package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.dictionaries.flightType.FlightType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "Invalid time format. Use HH:mm in 24-hour format.")
    private LocalTime flightDepartureTime; //hour:minute

    @Column(nullable = false)
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "Invalid time format. Use HH:mm in 24-hour format.")
    private LocalTime flightArrivalTime; //hour:minute

    @Column(nullable = false)
    private String flightDuration; //hour:minute

    @Column(nullable = false)
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Invalid date format. Use yyyy-MM-dd.")
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
    @Pattern(regexp = "^[1-9]$|^[1-9]\\d$|^300$", message = "Invalid number of seats. Must be a number between 1 and 300.")
    private int flightSeatsNumber;

}
