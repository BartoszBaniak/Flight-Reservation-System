package com.reservation.system.flight;

import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportService;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flightConnection.FlightConnectionEntity;
import com.reservation.system.flightConnection.FlightConnectionService;
import com.reservation.system.reservation.ReservationCreateRequest;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatService;
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

    private final AirportService airportService;
    private final SeatService seatService;
    private final FlightConnectionService flightConnectionService;

    @Transactional
    public FlightCreateResponse createFlight(FlightCreateRequest flightCreateRequest) {

        if(checkIfFlightExists(flightCreateRequest)) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_EXISTS_MESSAGE).code(1L).build();
        }

        String flightDuration = formatFlightDuration(flightCreateRequest.getFlightDepartureTime(), flightCreateRequest.getFlightArrivalTime());

        FlightConnectionEntity flightConnectionEntity = flightConnectionService.createFlightConnectionEntity(
                airportService.getAirportEntityByCode(flightCreateRequest.getFlightDeparture().getAirportCode()),
                airportService.getAirportEntityByCode(flightCreateRequest.getFlightArrival().getAirportCode()));

        flightRepository.save(createFlightEntity(flightCreateRequest, flightDuration, flightConnectionEntity));

        List<SeatEntity> seatEntities = seatService.generateAndGetAvailableSeats(flightCreateRequest.getFlightSeatsNumber(),
                flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightConnectionEntity.getDepartureAirport(),
                        flightConnectionEntity.getArrivalAirport(),
                        flightCreateRequest.getFlightDate(),
                        flightCreateRequest.getFlightDepartureTime()).get());

        seatService.saveAllSeats(seatEntities);

        return FlightCreateResponse.builder().data(FLIGHT_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public FlightIdentifierResponse updateFlight(FlightUpdateRequest flightUpdateRequest) {

        FlightIdentifierRequest flightIdentifierRequest = flightUpdateRequest.getFlightUpdateRequestIdentifier();
        FlightUpdateRequestDto flightUpdateRequestDto = flightUpdateRequest.getFlightUpdateRequestDto();

        FlightEntity existingFlight = flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        airportService.getAirportEntityByCode(flightIdentifierRequest.getFlightDeparture().getAirportCode()),
                        airportService.getAirportEntityByCode(flightIdentifierRequest.getFlightArrival().getAirportCode()),
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
                airportService.getAirportEntityByCode(flightIdentifierRequest.getFlightDeparture().getAirportCode()),
                airportService.getAirportEntityByCode(flightIdentifierRequest.getFlightArrival().getAirportCode()),
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

    public FlightEntity getFlightEntity(ReservationCreateRequest reservationCreateRequest) {

        AirportEntity flightDeparture = airportService.getAirportByCity(reservationCreateRequest.getFlightDeparture());
        AirportEntity flightArrival = airportService.getAirportByCity(reservationCreateRequest.getFlightArrival());

        return flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightDeparture,
                        flightArrival,
                        reservationCreateRequest.getFlightDate(),
                        reservationCreateRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(1L).build());

    }

    private FlightEntity createFlightEntity(FlightCreateRequest flightCreateRequest, String flightDuration, FlightConnectionEntity flightConnectionEntity) {

        return FlightEntity.builder()
                .flightDeparture(flightConnectionEntity.getDepartureAirport())
                .flightArrival(flightConnectionEntity.getArrivalAirport())
                .flightDepartureTime(flightCreateRequest.getFlightDepartureTime())
                .flightArrivalTime(flightCreateRequest.getFlightArrivalTime())
                .flightDuration(flightDuration)
                .flightDate(flightCreateRequest.getFlightDate())
                .flightConnection(flightConnectionEntity)
                .flightStatus(FlightStatus.SCHEDULED)
                .flightType(flightCreateRequest.getFlightType())
                .flightSeatsNumber(flightCreateRequest.getFlightSeatsNumber())
                .build();
    }

    private void updateFlightEntity(FlightEntity existingFlight, FlightUpdateRequestDto flightUpdateRequestDto, String flightDuration) {

        existingFlight.setFlightConnection(
                flightConnectionService.createFlightConnection(
                        airportService.getAirportEntityByCode(flightUpdateRequestDto.getFlightDeparture().getAirportCode()),
                        airportService.getAirportEntityByCode(flightUpdateRequestDto.getFlightArrival().getAirportCode())));
        existingFlight.setFlightDeparture(airportService.getAirportEntityByCode(flightUpdateRequestDto.getFlightDeparture().getAirportCode()));
        existingFlight.setFlightArrival(airportService.getAirportEntityByCode(flightUpdateRequestDto.getFlightArrival().getAirportCode()));
        existingFlight.setFlightDepartureTime(flightUpdateRequestDto.getFlightDepartureTime());
        existingFlight.setFlightArrivalTime(flightUpdateRequestDto.getFlightArrivalTime());
        existingFlight.setFlightDuration(flightDuration);
        existingFlight.setFlightDate(flightUpdateRequestDto.getFlightDate());
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

        FlightConnectionEntity flightConnection = flightConnectionService.createFlightConnection(
                airportService.getAirportEntityByCode(flightCreateRequest.getFlightDeparture().getAirportCode()),
                airportService.getAirportEntityByCode(flightCreateRequest.getFlightArrival().getAirportCode()));

        if(flightConnection != null) {
            return flightRepository.existsByFlightConnectionAndFlightDateAndFlightDepartureTime(
                    flightConnection,
                    flightCreateRequest.getFlightDate(),
                    flightCreateRequest.getFlightDepartureTime());
        }

        return false;
    }

    private FlightEntity searchForFlight(FlightIdentifierRequest flightIdentifierRequest) {

        return flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        airportService.getAirportEntityByCode(flightIdentifierRequest.getFlightDeparture().getAirportCode()),
                        airportService.getAirportEntityByCode(flightIdentifierRequest.getFlightArrival().getAirportCode()),
                        flightIdentifierRequest.getFlightDate(),
                        flightIdentifierRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(1L).build());
    }

    private FlightDto mapToFlightDto(FlightEntity flightEntity) {

        return FlightDto.builder()
                .flightDeparture(airportService.mapToAirportDto(flightEntity.getFlightDeparture()))
                .flightArrival(airportService.mapToAirportDto(flightEntity.getFlightArrival()))
                .flightDepartureTime(flightEntity.getFlightDepartureTime())
                .flightArrivalTime(flightEntity.getFlightArrivalTime())
                .flightDuration(flightEntity.getFlightDuration())
                .flightDate(flightEntity.getFlightDate())
                .flightNumber(flightEntity.getFlightConnection().getFlightNumber())
                .flightType(flightEntity.getFlightType())
                .flightStatus(flightEntity.getFlightStatus())
                .flightSeatsNumber(flightEntity.getFlightSeatsNumber())
                .build();
    }

}