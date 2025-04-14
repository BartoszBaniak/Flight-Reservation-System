package com.reservation.system.flight;

import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.exceptions.InternalBusinessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    public static final String TIME_STRING = "%02d:%02d";
    public static final String FLIGHT_CREATED_MESSAGE = "Flight created successfully";
    public static final String FLIGHT_DELETED_MESSAGE = "Flight deleted successfully";
    public static final String FLIGHT_UPDATED_MESSAGE = "Flight updated successfully";
    public static final String FLIGHT_EXISTS_MESSAGE = "Flight already exists";
    public static final String FLIGHT_NOT_FOUND_MESSAGE = "Flight not found";
    private final FlightRepository flightRepository;

    @Transactional
    public FlightCreateResponse createFlight(FlightCreateRequest flightCreateRequest) {

        if(checkIfFlightExists(flightCreateRequest)) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_EXISTS_MESSAGE).code(1L).build();
        }
        String flightDuration = formatFlightDuration(flightCreateRequest.getFlightDepartureTime(), flightCreateRequest.getFlightArrivalTime());
        //Optional<FlightNumber> flightNumber = FlightNumberService.getFlightNumberByAirports(flightCreateRequest.getFlightDeparture(), flightCreateRequest.getFlightArrival()); //TODO przypisanie numeru lotu po miejscu przylotu i odlotu
        flightRepository.save(createFlightEntity(flightCreateRequest, flightDuration));

        return FlightCreateResponse.builder().data(FLIGHT_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();

    }

    @Transactional
    public FlightIdentifierResponse updateFlight(FlightUpdateRequest flightUpdateRequest) {

        FlightIdentifierRequest flightIdentifierRequest = flightUpdateRequest.getFlightUpdateRequestIdentifier();
        FlightUpdateRequestDto flightUpdateRequestDto = flightUpdateRequest.getFlightUpdateRequestDto();

        FlightEntity existingFlight = flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                flightIdentifierRequest.getFlightDeparture(),
                        flightIdentifierRequest.getFlightArrival(),
                        flightIdentifierRequest.getFlightDate(),
                        flightIdentifierRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(2L).build()); //todo przeniesc do private method

        String flightDuration = formatFlightDuration(flightUpdateRequestDto.getFlightDepartureTime(), flightUpdateRequestDto.getFlightArrivalTime());

        updateFlightEntity(existingFlight, flightUpdateRequestDto, flightDuration);

        return FlightIdentifierResponse.builder().data(FLIGHT_UPDATED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public FlightIdentifierResponse deleteFlight(FlightIdentifierRequest flightIdentifierRequest) {

        searchForFlight(flightIdentifierRequest);
        flightRepository.deleteByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                flightIdentifierRequest.getFlightDeparture(),
                flightIdentifierRequest.getFlightArrival(),
                flightIdentifierRequest.getFlightDate(),
                flightIdentifierRequest.getFlightDepartureTime()
        );

        return FlightIdentifierResponse.builder().data(FLIGHT_DELETED_MESSAGE).warnings(List.of()).errors(List.of()).build();

    }

    public FlightReadResponse getFlight(FlightIdentifierRequest flightIdentifierRequest) {

        FlightEntity flightEntity = searchForFlight(flightIdentifierRequest);
        FlightDto flightDto = mapToFlightDto(flightEntity);

        return FlightReadResponse.builder()
                .data(flightDto)
                .warnings(List.of())
                .errors(List.of())
                .build();
    }

    public List<FlightReadResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::mapToFlightDto)
                .map(flightDto -> FlightReadResponse.builder()
                        .data(flightDto)
                        .warnings(List.of())
                        .errors(List.of())
                        .build())
                .toList();
    }

    private FlightEntity createFlightEntity(FlightCreateRequest flightCreateRequest, String flightDuration) {
        return FlightEntity.builder()
                .flightDeparture(flightCreateRequest.getFlightDeparture())
                .flightArrival(flightCreateRequest.getFlightArrival())
                .flightDepartureTime(flightCreateRequest.getFlightDepartureTime())
                .flightArrivalTime(flightCreateRequest.getFlightArrivalTime())
                .flightDuration(flightDuration)
                .flightDate(flightCreateRequest.getFlightDate())
                .flightNumber(flightCreateRequest.getFlightNumber())
                .flightStatus(FlightStatus.SCHEDULED)
                .flightType(flightCreateRequest.getFlightType())
                .flightSeatsNumber(flightCreateRequest.getFlightSeatsNumber())
                .build();
    }

    private void updateFlightEntity(FlightEntity existingFlight, FlightUpdateRequestDto flightUpdateRequestDto, String flightDuration) {
        existingFlight.setFlightDeparture(flightUpdateRequestDto.getFlightDeparture());
        existingFlight.setFlightArrival(flightUpdateRequestDto.getFlightArrival());
        existingFlight.setFlightDepartureTime(flightUpdateRequestDto.getFlightDepartureTime());
        existingFlight.setFlightArrivalTime(flightUpdateRequestDto.getFlightArrivalTime());
        existingFlight.setFlightDuration(flightDuration);
        existingFlight.setFlightDate(flightUpdateRequestDto.getFlightDate());
        existingFlight.setFlightNumber(flightUpdateRequestDto.getFlightNumber());
        existingFlight.setFlightSeatsNumber(flightUpdateRequestDto.getFlightSeatsNumber());
    }

    private String formatFlightDuration(LocalTime departureTime, LocalTime arrivalTime) {
        Duration duration = Duration.between(departureTime, arrivalTime);
        long durationInMinutes = duration.toMinutes();
        int hours = (int) (durationInMinutes / 60);
        int minutes = (int) (durationInMinutes % 60);
        return String.format(TIME_STRING, hours, minutes);
    }

    private boolean checkIfFlightExists(FlightCreateRequest flightCreateRequest) {

        return flightRepository.existsByFlightNumberAndFlightDateAndFlightDepartureTime(
                        flightCreateRequest.getFlightNumber(),
                        flightCreateRequest.getFlightDate(),
                        flightCreateRequest.getFlightDepartureTime());
    }

    private FlightEntity searchForFlight(FlightIdentifierRequest flightIdentifierRequest) {
        return flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightIdentifierRequest.getFlightDeparture(), flightIdentifierRequest.getFlightArrival(), flightIdentifierRequest.getFlightDate(), flightIdentifierRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(1L).build());
    }

    private FlightDto mapToFlightDto(FlightEntity flightEntity) {
        return FlightDto.builder()
                .flightDeparture(flightEntity.getFlightDeparture())
                .flightArrival(flightEntity.getFlightArrival())
                .flightDepartureTime(flightEntity.getFlightDepartureTime())
                .flightArrivalTime(flightEntity.getFlightArrivalTime())
                .flightDuration(flightEntity.getFlightDuration())
                .flightDate(flightEntity.getFlightDate())
                .flightNumber(flightEntity.getFlightNumber())
                .flightType(flightEntity.getFlightType())
                .flightStatus(flightEntity.getFlightStatus())
                .flightSeatsNumber(flightEntity.getFlightSeatsNumber())
                .build();
    }

}