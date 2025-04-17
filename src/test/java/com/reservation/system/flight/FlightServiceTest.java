package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.airport.AirportEntity;
import com.reservation.system.flightConnection.FlightConnectionEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

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

    @Test
    void getAllFlights_ShouldReturnFlights() {
        // given
        FlightEntity flightEntity = createFlightEntity();
        when(flightRepository.findAll()).thenReturn(List.of(flightEntity));

        // when
        List<FlightReadResponse> response = flightService.getAllFlights();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getData()).isNotNull();
        assertThat(response.get(0)).isEqualTo(flightEntity);
    }

    private FlightEntity createFlightEntity() {
        // given
        AirportEntity departure = new AirportEntity();
        departure.setAirportCode("WAW");
        departure.setAirportCity("Warsaw");

        AirportEntity arrival = new AirportEntity();
        arrival.setAirportCode("LHR");
        arrival.setAirportCity("Londyn");

        FlightConnectionEntity flightConnectionEntity = new FlightConnectionEntity();
        flightConnectionEntity.setDepartureAirport(departure);
        flightConnectionEntity.setArrivalAirport(arrival);
        flightConnectionEntity.setFlightNumber("WAW-LHR-01");

        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setFlightDeparture(departure);
        flightEntity.setFlightArrival(arrival);
        flightEntity.setFlightDepartureTime(LocalTime.of(10, 30));
        flightEntity.setFlightArrivalTime(LocalTime.of(14, 30));
        flightEntity.setFlightDate(LocalDate.of(2025, 5, 10));
        flightEntity.setFlightConnection(flightConnectionEntity);
        flightEntity.setFlightDuration("04:00");
        flightEntity.setFlightSeatsNumber(200);

        return flightEntity;
    }

//    private FlightDto createFlightDto(){
//        FlightDto flightDto = FlightDto.builder().flightArrival().;
//        flightDto.setFlightDeparture();
//        flightDto.setFlightArrival(new AirportDto());
//        flightDto.setFlightDepartureTime(LocalTime.of(10, 30));
//        flightDto.setFlightArrivalTime(LocalTime.of(14, 30));
//        flightDto.setFlightDate(LocalDate.of(2025, 5, 10));
//        flightDto.setFlightDuration("04:00");
//        flightDto.setFlightNumber("WAW-LHR-01");
//        flightDto.setFlightSeatsNumber(200);
//        return flightDto;
//    }
}
