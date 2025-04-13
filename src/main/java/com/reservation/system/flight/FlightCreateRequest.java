package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.dictionaries.flightType.FlightType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class FlightCreateRequest {

    private Airport flightDeparture;
    private Airport flightArrival;
    private LocalTime flightDepartureTime; //hour:minute
    private LocalTime flightArrivalTime; //hour:minute
    private LocalDate flightDate;
    private FlightNumber flightNumber;
    private FlightType flightType;
    private int flightSeatsNumber;

}
