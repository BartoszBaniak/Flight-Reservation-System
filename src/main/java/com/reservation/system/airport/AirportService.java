package com.reservation.system.airport;

import com.reservation.system.exceptions.InternalBusinessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirportService {

    public static final String AIRPORT_NOT_FOUND_MESSAGE = "Airport not found";

    private final AirportRepository airportRepository;

    public AirportEntity getAirportByCity(String airportCity) {

        return airportRepository.findByAirportCity(airportCity)
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(AIRPORT_NOT_FOUND_MESSAGE).code(1L).build());
    }

    public AirportEntity getAirportEntityByCode(String airportCode) {
        return airportRepository.findByAirportCode(airportCode)
                .orElseThrow(() -> InternalBusinessException.builder()
                        .type(HttpStatus.BAD_REQUEST)
                        .message(AIRPORT_NOT_FOUND_MESSAGE)
                        .code(1L).build());
    }

    public AirportDto mapToAirportDto(AirportEntity airportEntity) {

        return AirportDto.builder()
                .airportCity(airportEntity.getAirportCity())
                .airportCode(airportEntity.getAirportCode())
                .build();

    }
}
