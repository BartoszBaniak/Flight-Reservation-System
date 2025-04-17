package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class FlightIdentifierRequest {

    private AirportDto flightDeparture;
    private AirportDto flightArrival;
    private LocalTime flightDepartureTime;
    private LocalDate flightDate;
}