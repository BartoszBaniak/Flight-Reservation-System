package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportService;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flightConnection.FlightConnectionEntity;
import com.reservation.system.flightConnection.FlightConnectionService;
import com.reservation.system.seat.SeatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportService airportService;

    @Mock
    private SeatService seatService;

    @Mock
    private FlightConnectionService flightConnectionService;

    @InjectMocks
    private FlightService flightService;

    @Test
    void createFlight_ShouldCreateFlight_WhenFlightDoesNotExist() {
        // given
        FlightCreateRequest request = buildSampleCreateRequest();

        AirportEntity departure = new AirportEntity();
        departure.setAirportCode("WAW");
        departure.setAirportCity("Warszawa");

        AirportEntity arrival = new AirportEntity();
        arrival.setAirportCode("LHR");
        arrival.setAirportCity("Londyn");

        FlightConnectionEntity connection = FlightConnectionEntity.builder()
                .departureAirport(departure)
                .arrivalAirport(arrival)
                .flightNumber("LO123")
                .build();

        when(airportService.getAirportEntityByCode("WAW")).thenReturn(departure);
        when(airportService.getAirportEntityByCode("LHR")).thenReturn(arrival);
        when(flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(any(), any(), any(), any()))
                .thenReturn(null);
        when(flightConnectionService.createFlightConnection(departure, arrival)).thenReturn(connection);
        when(flightRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(seatService.generateAndGetAvailableSeats(anyInt(), any())).thenReturn(List.of());
        doNothing().when(seatService).saveAllSeats(anyList());

        // when
        FlightCreateResponse response = flightService.createFlight(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo(FlightService.FLIGHT_CREATED_MESSAGE);
        assertThat(response.getWarnings()).isEmpty();
        assertThat(response.getErrors()).isEmpty();

        verify(flightRepository).save(any());
        verify(seatService).saveAllSeats(anyList());
    }

//    @Test
//    void updateFlight_shouldUpdateFlightDetails_whenFlightExists() {
//        // given
//        FlightEntity existingFlight = createFlightEntity();
//        FlightUpdateRequest updateRequest = FlightUpdateRequest.builder()
//                .flightNumber("LO124")
//                .flightSeatsNumber(250)
//                .build();
//
//        when(flightRepository.findById(anyInt())).thenReturn(Optional.of(existingFlight));
//        when(flightRepository.save(any(FlightEntity.class))).thenReturn(existingFlight);
//
//        // when
//        FlightEntity updatedFlight = flightService.updateFlight(existingFlight, updateRequest);
//
//        // then
//        assertThat(updatedFlight).isNotNull();
//        assertThat(updatedFlight.getFlightNumber()).isEqualTo("LO124");
//        assertThat(updatedFlight.getFlightSeatsNumber()).isEqualTo(250);
//    }


    @Test
    void deleteFlight_ShouldDeleteFlightSuccessfully() {
        // given
        FlightIdentifierRequest request = buildSampleIdentifierRequest();

        FlightEntity flight = new FlightEntity();

        when(airportService.getAirportEntityByCode(anyString())).thenReturn(new AirportEntity());
        when(flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(any(), any(), any(), any()))
                .thenReturn(flight);

        doNothing().when(seatService).deleteSeatsByFlight(flight);
        doNothing().when(flightRepository).deleteByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(any(), any(), any(), any());

        // when
        FlightIdentifierResponse response = flightService.deleteFlight(request);

        // then
        verify(seatService).deleteSeatsByFlight(flight);
        verify(flightRepository).deleteByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(any(), any(), any(), any());
        assertThat(response.getData()).isEqualTo(FlightService.FLIGHT_DELETED_MESSAGE);
    }

    @Test
    void getAllFlights_ShouldReturnNotNullList() {
        // given
        when(flightRepository.findAll()).thenReturn(List.of());

        // when
        List<FlightReadResponse> response = flightService.getAllFlights();

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

//    @Test
//    void getAllFlights_ShouldReturnFlights() {
//        // given
//        FlightDto flightDto = createFlightDto();
//        FlightEntity flightEntity = createFlightEntity();
//        when(flightRepository.findAll()).thenReturn(List.of(flightEntity));
//
//        // when
//        List<FlightReadResponse> response = flightService.getAllFlights();
//
//        // then
//        assertThat(response).isNotNull();
//        assertThat(response).hasSize(1);
//        FlightReadResponse flightReadResponse = response.get(0);
//        assertThat(flightReadResponse.getData().getFlightDeparture().getAirportCode()).isEqualTo(flightDto.getFlightDeparture().getAirportCode());
//        assertThat(flightReadResponse.getData().getFlightArrival().getAirportCode()).isEqualTo(flightDto.getFlightArrival().getAirportCode());
//    }

    @Test
    void createFlight_shouldThrowExceptionWhenFlightAlreadyExists() {
        // given
        FlightCreateRequest request = buildSampleCreateRequest();
        when(flightRepository.existsByFlightConnectionAndFlightDateAndFlightDepartureTimeAndFlightArrivalTime(any(), any(), any(), any()))
                .thenReturn(true);

        // when, then
        assertThrows(InternalBusinessException.class, () -> {
            flightService.createFlight(request);
        });
    }


    private FlightEntity createFlightEntity() {
        // given
        AirportEntity departure = new AirportEntity();
        departure.setAirportCode("WAW");
        departure.setAirportCity("Warszawa");

        AirportEntity arrival = new AirportEntity();
        arrival.setAirportCode("LHR");
        arrival.setAirportCity("Londyn");

        FlightConnectionEntity flightConnectionEntity = new FlightConnectionEntity();
        flightConnectionEntity.setDepartureAirport(departure);
        flightConnectionEntity.setArrivalAirport(arrival);

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightDeparture(departure);
        flightEntity.setFlightArrival(arrival);
        flightEntity.setFlightDepartureTime(LocalTime.of(10, 30));
        flightEntity.setFlightArrivalTime(LocalTime.of(14, 30));
        flightEntity.setFlightDate(LocalDate.of(2025, 9, 19));
        flightEntity.setFlightConnection(flightConnectionEntity);
        flightEntity.setFlightDuration("04:00");
        flightEntity.setFlightSeatsNumber(200);

        return flightEntity;
    }

    private FlightCreateRequest buildSampleCreateRequest() {
        return FlightCreateRequest.builder()
                .flightDeparture(new AirportDto("WAW", "Warszawa"))
                .flightArrival(new AirportDto("LHR", "Londyn"))
                .flightDate(LocalDate.now())
                .flightDepartureTime(LocalTime.of(12, 0))
                .flightArrivalTime(LocalTime.of(15, 0))
                .flightSeatsNumber(180)
                .build();
    }

    private FlightIdentifierRequest buildSampleIdentifierRequest() {
        return FlightIdentifierRequest.builder()
                .flightDeparture(new AirportDto("WAW", "Warszawa"))
                .flightArrival(new AirportDto("LHR", "Londyn"))
                .flightDate(LocalDate.now())
                .flightDepartureTime(LocalTime.of(12, 0))
                .build();
    }

    private FlightDto createFlightDto(){
        AirportDto departureAirport = new AirportDto("WAW", "Warszawa");
        AirportDto arrivalAirport = new AirportDto("LHR", "Londyn");


        return FlightDto.builder()
                .flightDeparture(departureAirport)
                .flightArrival(arrivalAirport)
                .flightDepartureTime(LocalTime.of(10, 30))
                .flightArrivalTime(LocalTime.of(14, 30))
                .flightDate(LocalDate.of(2025, 5, 10))
                .flightDuration("04:00")
                .flightNumber("WAW-LHR-01")
                .flightSeatsNumber(200)
                .build();
    }
}
