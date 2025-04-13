package com.reservation.system.flight;

import com.reservation.system.dictionaries.airport.Airport;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class FlightReadRequest {

    private Airport flightDeparture;
    private Airport flightArrival;
    private LocalTime flightDepartureTime;
    private LocalDate flightDate;
}
