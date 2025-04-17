package com.reservation.system.flightConnection;

import com.reservation.system.airport.AirportEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightConnectionTest {

    @InjectMocks
    private FlightConnectionService flightConnectionService;

    @Mock
    private FlightConnectionRepository flightConnectionRepository;

    private AirportEntity depAirport;
    private AirportEntity arrAirport;

    @BeforeEach
    void setUp() {
        depAirport = AirportEntity.builder().airportCode("WAW").build();
        arrAirport = AirportEntity.builder().airportCode("JFK").build();
    }

    @Test
    void createFlightConnection_shouldReturnExistingConnectionIfFound() {

        FlightConnectionEntity existing = FlightConnectionEntity.builder()
                .departureAirport(depAirport)
                .arrivalAirport(arrAirport)
                .flightNumber("LO100")
                .build();

        when(flightConnectionRepository.findByDepartureAirportAndArrivalAirport(depAirport, arrAirport))
                .thenReturn(existing);

        var result = flightConnectionService.createFlightConnection(depAirport, arrAirport);

        assertThat(result).isEqualTo(existing);
        verify(flightConnectionRepository, never()).save(any());
    }

    @Test
    void createFlightConnection_shouldCreateNewConnectionIfNotFound() {

        when(flightConnectionRepository.findByDepartureAirportAndArrivalAirport(depAirport, arrAirport))
                .thenReturn(null);


        when(flightConnectionRepository.findTopByOrderByFlightNumberDesc())
                .thenReturn(null);

        ArgumentCaptor<FlightConnectionEntity> captor = ArgumentCaptor.forClass(FlightConnectionEntity.class);

        when(flightConnectionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = flightConnectionService.createFlightConnection(depAirport, arrAirport);

        verify(flightConnectionRepository).save(captor.capture());
        assertThat(captor.getValue().getFlightNumber()).isEqualTo("LO100");
        assertThat(result.getDepartureAirport()).isEqualTo(depAirport);
        assertThat(result.getArrivalAirport()).isEqualTo(arrAirport);
    }

    @Test
    void generateFlightNumber_shouldReturnNextNumberIfNoneExists() {

        when(flightConnectionRepository.findByDepartureAirportAndArrivalAirport(depAirport, arrAirport))
                .thenReturn(null);

        when(flightConnectionRepository.findTopByOrderByFlightNumberDesc())
                .thenReturn(null);

        String result = flightConnectionService.generateFlightNumber(depAirport, arrAirport);
        assertThat(result).isEqualTo("LO100");
    }

    @Test
    void generateFlightNumber_shouldReturnExistingFlightNumberIfConnectionExists() {

        when(flightConnectionRepository.findByDepartureAirportAndArrivalAirport(depAirport, arrAirport))
                .thenReturn(FlightConnectionEntity.builder().flightNumber("LO999").build());

        String result = flightConnectionService.generateFlightNumber(depAirport, arrAirport);

        assertThat(result).isEqualTo("LO999");
    }

    @Test
    void generateFlightNumber_shouldIncrementIfNoConnectionButLatestExists() {

        when(flightConnectionRepository.findByDepartureAirportAndArrivalAirport(depAirport, arrAirport))
                .thenReturn(null);

        when(flightConnectionRepository.findTopByOrderByFlightNumberDesc())
                .thenReturn(FlightConnectionEntity.builder().flightNumber("LO105").build());

        String result = flightConnectionService.generateFlightNumber(depAirport, arrAirport);

        assertThat(result).isEqualTo("LO106");
    }
}

