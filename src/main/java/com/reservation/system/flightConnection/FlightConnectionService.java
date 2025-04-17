package com.reservation.system.flightConnection;

import com.reservation.system.airport.AirportEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FlightConnectionService {

    private final FlightConnectionRepository flightConnectionRepository;

    public FlightConnectionEntity createFlightConnection(AirportEntity flightDeparture, AirportEntity flightArrival) {

        FlightConnectionEntity existingConnection = flightConnectionRepository.findByDepartureAirportAndArrivalAirport(flightDeparture, flightArrival);
        if (existingConnection != null) {
            return existingConnection;
        }

        String flightNumber = generateFlightNumber(flightDeparture, flightArrival);

        FlightConnectionEntity newConnection = FlightConnectionEntity.builder()
                .departureAirport(flightDeparture)
                .arrivalAirport(flightArrival)
                .flightNumber(flightNumber)
                .build();

        return flightConnectionRepository.save(newConnection);
    }

    public String generateFlightNumber(AirportEntity flightDeparture, AirportEntity flightArrival) {

        FlightConnectionEntity existingConnection = flightConnectionRepository.findByDepartureAirportAndArrivalAirport(flightDeparture, flightArrival);

        if (existingConnection != null) {
            return existingConnection.getFlightNumber();
        }

        int nextFlightNumber = getNextFlightNumber();
        return "LO" + nextFlightNumber;

    }

    private int getNextFlightNumber() {

        FlightConnectionEntity latestConnection = flightConnectionRepository.findTopByOrderByFlightNumberDesc();

        if (latestConnection != null && latestConnection.getFlightNumber() != null) {
            String latestFlightNumber = latestConnection.getFlightNumber();
            try {
                int latestNumber = Integer.parseInt(latestFlightNumber.substring(2));
                return latestNumber + 1;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid flight number format: " + latestFlightNumber, e);
            }
        } else {
            return 100;
        }
    }
}
