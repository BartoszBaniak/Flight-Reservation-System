package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FlightUpdateRequestDto {

    private Airport flightDeparture;
    private Airport flightArrival;
    private LocalTime flightDepartureTime;
    private LocalTime flightArrivalTime;
    private String flightDuration;
    private LocalDate flightDate;
    private FlightNumber flightNumber;
    private int flightSeatsNumber;
}
