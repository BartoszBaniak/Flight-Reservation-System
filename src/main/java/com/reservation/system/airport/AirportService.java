package com.reservation.system.airport;

import com.reservation.system.exceptions.InternalBusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AirportService {

    public static final String AIRPORT_NOT_FOUND_MESSAGE = "Airport not found";

    private AirportRepository airportRepository;

    public AirportEntity getAirportByCity(String airportCity) {

        return airportRepository.findByAirportCity(airportCity)
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(AIRPORT_NOT_FOUND_MESSAGE).code(1L).build());
    }
}
