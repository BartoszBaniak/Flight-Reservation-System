package com.reservation.system.seat;

import com.reservation.system.flight.FlightEntity;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flight.FlightRepository;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    void saveAllSeats_ShouldSaveSeats() {
        // given
        List<SeatEntity> seatEntities = List.of(new SeatEntity(), new SeatEntity());

        // when
        seatService.saveAllSeats(seatEntities);

        // then
        verify(seatRepository).saveAll(seatEntities);
    }

    @Test
    void saveSeat_ShouldSaveSeat() {
        // given
        SeatEntity seatEntity = new SeatEntity();

        // when
        seatService.saveSeat(seatEntity);

        // then
        verify(seatRepository).save(seatEntity);
    }

    @Test
    void getSeatEntity_ShouldReturnSeat_WhenSeatExists() {
        // given
        FlightEntity flightEntity = new FlightEntity();
        String seatNumber = "1A";
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setSeatNumber(seatNumber);
        seatEntity.setFlightEntity(flightEntity);

        when(seatRepository.findByFlightEntityAndSeatNumber(flightEntity, seatNumber)).thenReturn(seatEntity);

        // when
        SeatEntity result = seatService.getSeatEntity(flightEntity, seatNumber);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getSeatNumber()).isEqualTo(seatNumber);
        assertThat(result.getFlightEntity()).isEqualTo(flightEntity);
    }

    @Test
    void getSeatEntity_ShouldThrowException_WhenSeatDoesNotExist() {
        // given
        FlightEntity flightEntity = new FlightEntity();
        String seatNumber = "1A";

        when(seatRepository.findByFlightEntityAndSeatNumber(flightEntity, seatNumber)).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> seatService.getSeatEntity(flightEntity, seatNumber))
                .isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining(SeatService.SEAT_NOT_FOUND_MESSAGE);
    }

    @Test
    void generateAndGetAvailableSeats_ShouldGenerateSeats() {
        // given
        int seatsNumber = 10;
        FlightEntity flightEntity = new FlightEntity();

        // when
        List<SeatEntity> result = seatService.generateAndGetAvailableSeats(seatsNumber, flightEntity);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(seatsNumber);
        assertThat(result.get(0).getSeatNumber()).isNotEmpty();
        assertThat(result.get(0).getFlightEntity()).isEqualTo(flightEntity);
    }

    @Test
    void checkIfSeatIsAvailable_ShouldThrowException_WhenSeatIsNotAvailable() {
        // given
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setAvailable(false);

        // when, then
        assertThatThrownBy(() -> seatService.checkIfSeatIsAvailable(seatEntity))
                .isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining(SeatService.SEAT_RESERVED_MESSAGE);
    }

    @Test
    void checkIfSeatIsAvailable_ShouldNotThrowException_WhenSeatIsAvailable() {
        // given
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setAvailable(true);

        // when
        seatService.checkIfSeatIsAvailable(seatEntity);

        // then
        // No exception is thrown
    }

    @Test
    void deleteSeatsByFlight_ShouldDeleteSeats() {
        // given
        FlightEntity flightEntity = new FlightEntity();

        // when
        seatService.deleteSeatsByFlight(flightEntity);

        // then
        verify(seatRepository).deleteByFlightEntity(flightEntity);
    }

    @Test
    void refreshSeatsForFlight_ShouldRefreshSeats() {
        // given
        FlightEntity flightEntity = new FlightEntity();
        int seatsNumber = 50;

        // when
        seatService.refreshSeatsForFlight(flightEntity, seatsNumber);

        // then
        verify(seatRepository).deleteByFlightEntity(flightEntity);
        verify(seatRepository).saveAll(anyList());
    }
}
