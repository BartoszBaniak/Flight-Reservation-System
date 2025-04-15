package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.airport.AirportEntity;
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

    private AirportDto flightDeparture;
    private AirportDto flightArrival;
    private LocalTime flightDepartureTime;
    private LocalTime flightArrivalTime;
    private String flightDuration;
    private LocalDate flightDate;
    private String flightNumber;
    private int flightSeatsNumber;
}
