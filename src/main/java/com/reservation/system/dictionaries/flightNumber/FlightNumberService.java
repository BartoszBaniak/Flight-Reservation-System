package com.reservation.system.dictionaries.flightNumber;

import com.reservation.system.dictionaries.airport.Airport;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class FlightNumberService {
    public static Optional<FlightNumber> getFlightNumberByAirports(Airport departure, Airport arrival) {
        return Arrays.stream(FlightNumber.values())
                .filter(fn -> fn.getAirportDeparture() == departure && fn.getAirportArrival() == arrival)
                .findFirst();
    }
}
