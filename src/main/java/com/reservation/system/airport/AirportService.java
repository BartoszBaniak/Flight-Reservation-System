package com.reservation.system.airport;

import com.reservation.system.exceptions.ErrorEnum;
import com.reservation.system.exceptions.InternalBusinessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AirportService {

    public static final String AIRPORT_NOT_FOUND_MESSAGE = "Airport not found";

    private final AirportRepository airportRepository;

    public AirportEntity getAirportByCity(String airportCity) {
        AirportEntity airport = airportRepository.findByAirportCity(airportCity);
        if (airport == null) {
            throw InternalBusinessException.builder()
                    .type(HttpStatus.BAD_REQUEST)
                    .message(AIRPORT_NOT_FOUND_MESSAGE)
                    .code(ErrorEnum.AIRPORT_NOT_FOUND.getErrorCode())
                    .build();
        }
        return airport;
    }

    public AirportEntity getAirportEntityByCode(String airportCode) {
        AirportEntity airport = airportRepository.findByAirportCode(airportCode);
        if (airport == null) {
            throw InternalBusinessException.builder()
                    .type(HttpStatus.BAD_REQUEST)
                    .message(AIRPORT_NOT_FOUND_MESSAGE)
                    .code(1L)
                    .build();
        }
        return airport;
    }

    public AirportDto mapToAirportDto(AirportEntity airportEntity) {
        return AirportDto.builder()
                .airportCity(airportEntity.getAirportCity())
                .airportCode(airportEntity.getAirportCode())
                .build();

    }

    public List<AirportDto> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(this::mapToAirportDto)
                .toList();
    }
}
