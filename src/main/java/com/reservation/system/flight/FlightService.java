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

    public FlightReadResponse getFlight(FlightReadDeleteRequest flightReadDeleteRequest) {

        FlightEntity flightEntity = searchForFlight(flightReadDeleteRequest);
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

    private FlightEntity searchForFlight(FlightReadDeleteRequest flightReadDeleteRequest) {
        return flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightReadDeleteRequest.getFlightDeparture(), flightReadDeleteRequest.getFlightArrival(), flightReadDeleteRequest.getFlightDate(), flightReadDeleteRequest.getFlightDepartureTime())
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

    @Transactional
    public FlightDeleteResponse deleteFlight(FlightReadDeleteRequest flightReadDeleteRequest) {

        searchForFlight(flightReadDeleteRequest);
        flightRepository.deleteByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                flightReadDeleteRequest.getFlightDeparture(),
                flightReadDeleteRequest.getFlightArrival(),
                flightReadDeleteRequest.getFlightDate(),
                flightReadDeleteRequest.getFlightDepartureTime());

        return FlightDeleteResponse.builder().data(FLIGHT_DELETED_MESSAGE).warnings(List.of()).errors(List.of()).build();

    }
}

//
//    public ApiResponse<FlightUpdateResponse> updateFlight(int flightId, FlightUpdateRequest flightUpdateRequest) {
//
//        FlightEntity existingFlight = flightRepository.findById(flightId)
//                .orElseThrow(() -> new IllegalStateException("Flight with ID: " + flightId + " not found"));
//
//        existingFlight.setFlightDeparture(flightUpdateRequest.getFlightDeparture());
//        existingFlight.setFlightArrival(flightUpdateRequest.getFlightArrival());
//        existingFlight.setFlightDepartureTime(flightUpdateRequest.getFlightDepartureTime());
//        existingFlight.setFlightArrivalTime(flightUpdateRequest.getFlightArrivalTime());
//        existingFlight.setFlightDuration(flightUpdateRequest.getFlightDuration());
//        existingFlight.setFlightDate(flightUpdateRequest.getFlightDate());
//        existingFlight.setFlightNumber(flightUpdateRequest.getFlightNumber());
//        existingFlight.setFlightType(flightUpdateRequest.getFlightType());
//        existingFlight.setFlightSeatsNumber(flightUpdateRequest.getFlightSeatsNumber());
//
//    }
//
//    public ApiResponse<String> deleteFlight(int flightId) {
//
//        if(!flightRepository.existsById(flightId)) {
//            return new ApiResponse<>(LocalDateTime.now(), "Flight not found", "Flight with ID: " + flightId + " not found");
//        } else {
//            flightRepository.deleteById(flightId);
//            return new ApiResponse<>(LocalDateTime.now(), "Flight deleted successfully", "Flight with ID: " + flightId + " deleted successfully");
//        }
//    }
