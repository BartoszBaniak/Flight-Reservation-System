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
        return flightConnectionRepository.findByDepartureAirportAndArrivalAirport(flightDeparture, flightArrival)
                .orElseGet(() -> {
                    String flightNumber = generateFlightNumber(flightDeparture, flightArrival);

                    FlightConnectionEntity newConnection = FlightConnectionEntity.builder()
                            .departureAirport(flightDeparture)
                            .arrivalAirport(flightArrival)
                            .flightNumber(flightNumber)
                            .build();

                    return flightConnectionRepository.save(newConnection);
                });
    }

    public String generateFlightNumber(AirportEntity flightDeparture, AirportEntity flightArrival) {

        Optional<FlightConnectionEntity> existingConnection = flightConnectionRepository.findByDepartureAirportAndArrivalAirport(flightDeparture, flightArrival);

        if(existingConnection.isPresent()) {
            return existingConnection.get().getFlightNumber();
        }

        int nextFlightNumber = getNextFlightNumber();

        return "LO" + nextFlightNumber;

    }

    private int getNextFlightNumber() {

        Optional<FlightConnectionEntity> latestConnection = flightConnectionRepository.findTopByOrderByFlightNumberDesc();

        if(latestConnection.isPresent()) {
            String latestFlightNumber = latestConnection.get().getFlightNumber();
            return  Integer.parseInt(latestFlightNumber.substring(2)) + 1;

        } else {
            return 100;
        }
    }
}
