package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class FlightUpdateRequest {

    private Airport flightDeparture;
    private Airport flightArrival;
    private LocalTime flightDepartureTime;
    private LocalTime flightArrivalTime;
    private String flightDuration;
    private LocalDate flightDate;
    private FlightNumber flightNumber;
    private int flightSeatsNumber;
}
