package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.dictionaries.flightType.FlightType;
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
public class FlightDto {

    private AirportDto flightDeparture;
    private AirportDto flightArrival;
    private LocalTime flightDepartureTime;
    private LocalTime flightArrivalTime;
    private String flightDuration;
    private LocalDate flightDate;
    private String flightNumber;
    private FlightType flightType;
    private FlightStatus flightStatus;
    private int flightSeatsNumber;
}
