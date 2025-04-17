package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.dictionaries.flightType.FlightType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class FlightCreateRequest {

    private AirportDto flightDeparture;
    private AirportDto flightArrival;
    private LocalTime flightDepartureTime;
    private LocalTime flightArrivalTime;
    private LocalDate flightDate;
    private FlightType flightType;
    private int flightSeatsNumber;

}
