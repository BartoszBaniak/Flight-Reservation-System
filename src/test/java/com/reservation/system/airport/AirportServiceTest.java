package com.reservation.system.airport;

import com.reservation.system.exceptions.InternalBusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    @Test
    void getAirportByCity_ShouldReturnAirport_WhenAirportExists() {
        // given
        String airportCity = "Warszawa";
        AirportEntity airportEntity = new AirportEntity();
        airportEntity.setAirportCity(airportCity);
        airportEntity.setAirportCode("WAW");

        when(airportRepository.findByAirportCity(airportCity)).thenReturn(airportEntity);

        // when
        AirportEntity result = airportService.getAirportByCity(airportCity);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAirportCity()).isEqualTo(airportCity);
        assertThat(result.getAirportCode()).isEqualTo("WAW");
    }

    @Test
    void getAirportByCity_ShouldThrowException_WhenAirportDoesNotExist() {
        // given
        String airportCity = "NonExistentCity";

        when(airportRepository.findByAirportCity(airportCity)).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> airportService.getAirportByCity(airportCity))
                .isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining(AirportService.AIRPORT_NOT_FOUND_MESSAGE);
    }

    @Test
    void getAirportEntityByCode_ShouldReturnAirport_WhenAirportExists() {
        // given
        String airportCode = "WAW";
        AirportEntity airportEntity = new AirportEntity();
        airportEntity.setAirportCity("Warszawa");
        airportEntity.setAirportCode(airportCode);

        when(airportRepository.findByAirportCode(airportCode)).thenReturn(airportEntity);

        // when
        AirportEntity result = airportService.getAirportEntityByCode(airportCode);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAirportCode()).isEqualTo(airportCode);
        assertThat(result.getAirportCity()).isEqualTo("Warszawa");
    }

    @Test
    void getAirportEntityByCode_ShouldThrowException_WhenAirportDoesNotExist() {
        // given
        String airportCode = "XXX";

        when(airportRepository.findByAirportCode(airportCode)).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> airportService.getAirportEntityByCode(airportCode))
                .isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining(AirportService.AIRPORT_NOT_FOUND_MESSAGE);
    }

    @Test
    void mapToAirportDto_ShouldMapCorrectly() {
        // given
        AirportEntity airportEntity = new AirportEntity();
        airportEntity.setAirportCity("Warszawa");
        airportEntity.setAirportCode("WAW");

        // when
        AirportDto result = airportService.mapToAirportDto(airportEntity);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAirportCity()).isEqualTo("Warszawa");
        assertThat(result.getAirportCode()).isEqualTo("WAW");
    }
}
