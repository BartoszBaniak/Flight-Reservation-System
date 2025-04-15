package com.reservation.system.flightConnection;

import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportRepository;
import com.reservation.system.exceptions.InternalBusinessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FlightConnectionService {

    public static final String AIRPORT_NOT_FOUND_MESSAGE = "Airport not found";

    private final FlightConnectionRepository flightConnectionRepository;
    private final AirportRepository airportRepository;

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

    public FlightConnectionEntity createFlightConnectionEntity(AirportEntity flightDeparture, AirportEntity flightArrival) {

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

    private AirportEntity getAirportEntityByCode(String airportCode) {
        return airportRepository.findByAirportCode(airportCode)
                .orElseThrow(() -> InternalBusinessException.builder()
                        .type(HttpStatus.BAD_REQUEST)
                        .message(AIRPORT_NOT_FOUND_MESSAGE)
                        .code(1L).build());
    }
}
