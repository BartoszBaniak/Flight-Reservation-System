package com.reservation.system.passenger;

import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.reservation.ReservationEntity;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class PassengerServiceUnitTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private PassengerService passengerService;

    private final PassengerDto validPassengerDto = PassengerDto.builder()
            .firstName("Jan")
            .lastName("Kowalski")
            .email("jan.kowalski@example.com")
            .phoneNumber("123456789")
            .build();

    @Test
    void getPassenger_ShouldThrowException_WhenPassengerNotFound() {
        // given
        when(passengerRepository.getPassengerEntityByEmail(anyString())).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> passengerService.getPassenger(
                PassengerIdentifierRequest.builder().email("not.found@example.com").build()
        )).isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining("Passenger not found");
    }

    @Test
    void getAllPassengers_ShouldReturnNotNullList() {
        // given
        when(passengerRepository.findAll()).thenReturn(List.of());

        // when
        List<PassengerReadResponse> response = passengerService.getAllPassengers();

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    @Test
    void createPassenger_ShouldCreatePassenger() {
        // given
        when(passengerRepository.existsByEmailAndPhoneNumber(anyString(), anyString())).thenReturn(false);

        // when
        PassengerCreateResponse response = passengerService.createPassenger(validPassengerDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getData()).isEqualTo("Passenger created successfully.");
    }

    @Test
    void getPassenger_ShouldReturnPassengerReadResponse() {
        // given
        PassengerEntity entity = PassengerEntity.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .phoneNumber("123456789")
                .build();
        when(passengerRepository.getPassengerEntityByEmail(anyString())).thenReturn(entity);

        // when
        PassengerReadResponse response = passengerService.getPassenger(
                PassengerIdentifierRequest.builder().email("jan.kowalski@example.com").build()
        );

        // then
        assertThat(response).isNotNull();
        assertThat(response.getData().getEmail()).isEqualTo("jan.kowalski@example.com");
    }

    @Test
    void updatePassenger_ShouldUpdateFields() {
        // given
        PassengerUpdateRequest updateRequest = PassengerUpdateRequest.builder()
                .passengerIdentifierRequest(PassengerIdentifierRequest.builder().email("jan.kowalski@example.com").build())
                .passengerRequestDto(PassengerDto.builder().firstName("Janek").lastName("Nowak").email("jan.kowalski@example.com").phoneNumber("987654321").build())
                .build();

        PassengerEntity existingPassenger = PassengerEntity.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .phoneNumber("123456789")
                .build();

        when(passengerRepository.getPassengerEntityByEmail("jan.kowalski@example.com")).thenReturn(existingPassenger);

        // when
        PassengerIdentifierResponse response = passengerService.updatePassenger(updateRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(existingPassenger.getEmail()).isEqualTo("jan.kowalski@example.com");
    }

    @Test
    void updatePassenger_ShouldThrowException_WhenPassengerNotFound() {
        // given
        PassengerUpdateRequest updateRequest = PassengerUpdateRequest.builder()
                .passengerIdentifierRequest(PassengerIdentifierRequest.builder().email("ghost@example.com").build())
                .passengerRequestDto(validPassengerDto)
                .build();

        when(passengerRepository.getPassengerEntityByEmail("ghost@example.com")).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> passengerService.updatePassenger(updateRequest))
                .isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining("Passenger not found");
    }

    @Test
    void deletePassenger_ShouldCallDeleteAndSetSeatsAvailable() {
        // given
        SeatEntity seat = new SeatEntity();
        seat.setAvailable(false);

        ReservationEntity reservation = new ReservationEntity();
        reservation.setSeatEntity(seat);

        PassengerEntity passenger = new PassengerEntity();
        passenger.setReservations(List.of(reservation));
        passenger.setEmail("jan.kowalski@example.com");

        when(passengerRepository.getPassengerEntityByEmail("jan.kowalski@example.com")).thenReturn(passenger);

        // when
        PassengerIdentifierResponse response = passengerService.deletePassenger(
                PassengerIdentifierRequest.builder().email("jan.kowalski@example.com").build()
        );

        // then
        verify(seatService).saveSeat(seat);
        verify(passengerRepository).deleteByEmail("jan.kowalski@example.com");
        assertThat(response.getData()).isEqualTo("Passenger deleted successfully");
    }

    @Test
    void deletePassenger_ShouldNotCallSeatService_WhenNoReservations() {
        // given
        //PassengerEntity passenger = new PassengerEntity();
        PassengerEntity passenger = PassengerEntity.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .phoneNumber("123456789")
                .build();
        passenger.setReservations(new ArrayList<>());


        when(passengerRepository.getPassengerEntityByEmail("jan.kowalski@example.com")).thenReturn(passenger);

        // when
        PassengerIdentifierResponse response = passengerService.deletePassenger(
                PassengerIdentifierRequest.builder().email("jan.kowalski@example.com").build()
        );

        // then
        verify(seatService, never()).saveSeat(any(SeatEntity.class));
        verify(passengerRepository).deleteByEmail("jan.kowalski@example.com");
        assertThat(response.getData()).isEqualTo("Passenger deleted successfully");
    }

    @Test
    void deletePassenger_ShouldThrowException_WhenPassengerNotFound() {
        // given
        when(passengerRepository.getPassengerEntityByEmail("missing@example.com")).thenReturn(null);

        // when, then
        assertThatThrownBy(() -> passengerService.deletePassenger(
                PassengerIdentifierRequest.builder().email("missing@example.com").build()
        )).isInstanceOf(InternalBusinessException.class)
                .hasMessageContaining("Passenger not found");
    }


}
