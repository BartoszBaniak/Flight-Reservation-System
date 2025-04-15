package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportRepository;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flightConnection.FlightConnectionEntity;
import com.reservation.system.flightConnection.FlightConnectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FlightService {

    public static final String TIME_STRING = "%02d:%02d";
    public static final String FLIGHT_CREATED_MESSAGE = "Flight created successfully";
    public static final String FLIGHT_DELETED_MESSAGE = "Flight deleted successfully";
    public static final String FLIGHT_UPDATED_MESSAGE = "Flight updated successfully";
    public static final String FLIGHT_EXISTS_MESSAGE = "Flight already exists";
    public static final String FLIGHT_NOT_FOUND_MESSAGE = "Flight not found";
    public static final String AIRPORT_NOT_FOUND_MESSAGE = "Airport not found";

    private final FlightRepository flightRepository;
    private final FlightConnectionRepository flightConnectionRepository;
    private final AirportRepository airportRepository;

    @Transactional
    public FlightCreateResponse createFlight(FlightCreateRequest flightCreateRequest) {

        if(checkIfFlightExists(flightCreateRequest)) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_EXISTS_MESSAGE).code(1L).build();
        }

        String flightDuration = formatFlightDuration(flightCreateRequest.getFlightDepartureTime(), flightCreateRequest.getFlightArrivalTime());

        AirportEntity flightDeparture = airportRepository.findByAirportCode(flightCreateRequest.getFlightDeparture().getAirportCode())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(AIRPORT_NOT_FOUND_MESSAGE).code(1L).build());

        AirportEntity flightArrival = airportRepository.findByAirportCode(flightCreateRequest.getFlightArrival().getAirportCode())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(AIRPORT_NOT_FOUND_MESSAGE).code(1L).build());

        FlightConnectionEntity flightConnectionEntity = createFlightConnectionEntity(flightDeparture, flightArrival);

        flightRepository.save(createFlightEntity(flightCreateRequest, flightDuration, flightConnectionEntity));

        return FlightCreateResponse.builder().data(FLIGHT_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public FlightIdentifierResponse updateFlight(FlightUpdateRequest flightUpdateRequest) {

        FlightIdentifierRequest flightIdentifierRequest = flightUpdateRequest.getFlightUpdateRequestIdentifier();
        FlightUpdateRequestDto flightUpdateRequestDto = flightUpdateRequest.getFlightUpdateRequestDto();

        AirportEntity departureAirport = getAirportEntityByCode(flightIdentifierRequest.getFlightDeparture().getAirportCode());
        AirportEntity arrivalAirport = getAirportEntityByCode(flightIdentifierRequest.getFlightArrival().getAirportCode());

        FlightEntity existingFlight = flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        departureAirport,
                        arrivalAirport,
                        flightIdentifierRequest.getFlightDate(),
                        flightIdentifierRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(2L).build()); //todo przeniesc do private method

        String flightDuration = formatFlightDuration(flightUpdateRequestDto.getFlightDepartureTime(), flightUpdateRequestDto.getFlightArrivalTime());

        updateFlightEntity(existingFlight, flightUpdateRequestDto, flightDuration);

        return FlightIdentifierResponse.builder().data(FLIGHT_UPDATED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public FlightIdentifierResponse deleteFlight(FlightIdentifierRequest flightIdentifierRequest) {

        AirportEntity departureAirport = getAirportEntityByCode(flightIdentifierRequest.getFlightDeparture().getAirportCode());
        AirportEntity arrivalAirport = getAirportEntityByCode(flightIdentifierRequest.getFlightArrival().getAirportCode());

        searchForFlight(flightIdentifierRequest);
        flightRepository.deleteByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                departureAirport,
                arrivalAirport,
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

    private FlightEntity createFlightEntity(FlightCreateRequest flightCreateRequest, String flightDuration, FlightConnectionEntity flightConnectionEntity) {

        AirportEntity departureEntity = getAirportEntityByCode(flightCreateRequest.getFlightDeparture().getAirportCode());
        AirportEntity arrivalEntity = getAirportEntityByCode(flightCreateRequest.getFlightArrival().getAirportCode());

        return FlightEntity.builder()
                .flightDeparture(departureEntity)
                .flightArrival(arrivalEntity)
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

    private FlightConnectionEntity createFlightConnection(AirportEntity flightDeparture, AirportEntity flightArrival) {
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

    private FlightConnectionEntity createFlightConnectionEntity(AirportEntity flightDeparture, AirportEntity flightArrival) {

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

    private String generateFlightNumber(AirportEntity flightDeparture, AirportEntity flightArrival) {

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

    private void updateFlightEntity(FlightEntity existingFlight, FlightUpdateRequestDto flightUpdateRequestDto, String flightDuration) {


        AirportEntity departureEntity = getAirportEntityByCode(flightUpdateRequestDto.getFlightDeparture().getAirportCode());
        AirportEntity arrivalEntity = getAirportEntityByCode(flightUpdateRequestDto.getFlightArrival().getAirportCode());

        FlightConnectionEntity flightConnection = createFlightConnection(departureEntity, arrivalEntity);

        existingFlight.setFlightConnection(flightConnection);
        existingFlight.setFlightDeparture(departureEntity);
        existingFlight.setFlightArrival(arrivalEntity);
        existingFlight.setFlightDepartureTime(flightUpdateRequestDto.getFlightDepartureTime());
        existingFlight.setFlightArrivalTime(flightUpdateRequestDto.getFlightArrivalTime());
        existingFlight.setFlightDuration(flightDuration);
        existingFlight.setFlightDate(flightUpdateRequestDto.getFlightDate());
        //existingFlight.setFlightType(flightUpdateRequestDto.getFlightType()); // jak masz flightType, odkomentuj
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

        AirportEntity departureAirport = getAirportEntityByCode(flightCreateRequest.getFlightDeparture().getAirportCode());
        AirportEntity arrivalAirport = getAirportEntityByCode(flightCreateRequest.getFlightArrival().getAirportCode());


        FlightConnectionEntity flightConnection = createFlightConnection(departureAirport, arrivalAirport);

        if(flightConnection != null) {
            return flightRepository.existsByFlightConnectionAndFlightDateAndFlightDepartureTime(
                    flightConnection,
                    flightCreateRequest.getFlightDate(),
                    flightCreateRequest.getFlightDepartureTime());
        }

        return false;
    }

    private FlightEntity searchForFlight(FlightIdentifierRequest flightIdentifierRequest) {

        AirportEntity departureAirport = getAirportEntityByCode(flightIdentifierRequest.getFlightDeparture().getAirportCode());
        AirportEntity arrivalAirport = getAirportEntityByCode(flightIdentifierRequest.getFlightArrival().getAirportCode());

        return flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        departureAirport,
                        arrivalAirport,
                        flightIdentifierRequest.getFlightDate(),
                        flightIdentifierRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(1L).build());
    }


    private AirportDto mapToAirportDto(AirportEntity airportEntity) {

        return AirportDto.builder()
                .airportCity(airportEntity.getAirportCity())
                .airportCode(airportEntity.getAirportCode())
                .build();

    }

    private FlightDto mapToFlightDto(FlightEntity flightEntity) {

        return FlightDto.builder()
                .flightDeparture(mapToAirportDto(flightEntity.getFlightDeparture()))
                .flightArrival(mapToAirportDto(flightEntity.getFlightArrival()))
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

    private AirportEntity getAirportEntityByCode(String airportCode) {
        return airportRepository.findByAirportCode(airportCode)
                .orElseThrow(() -> InternalBusinessException.builder()
                        .type(HttpStatus.BAD_REQUEST)
                        .message(AIRPORT_NOT_FOUND_MESSAGE)
                        .code(1L).build());
    }

}