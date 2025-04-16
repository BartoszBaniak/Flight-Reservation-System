package com.reservation.system.reservation;

import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flight.FlightEntity;
import com.reservation.system.flight.FlightService;
import com.reservation.system.passenger.PassengerEntity;
import com.reservation.system.passenger.PassengerService;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    public static final String RESERVATION_NOT_FOUND_MESSAGE = "Reservation not found";
    public static final String RESERVATION_CREATED_MESSAGE = "Reservation created successfully";
    public static final String RESERVATION_UPDATED_MESSAGE = "Reservation updated successfully";
    public static final String RESERVATION_DELETED_MESSAGE = "Reservation deleted successfully";

    private final ReservationRepository reservationRepository;

    private final FlightService flightService;
    private final SeatService seatService;
    private final PassengerService passengerService;

    @Transactional
    public ReservationCreateResponse createReservation(ReservationCreateRequest reservationCreateRequest) {

        reservationRepository.save(createReservationEntity(reservationCreateRequest));

        return ReservationCreateResponse.builder().data(RESERVATION_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public ReservationIdentifierResponse updateReservation(ReservationUpdateRequest reservationUpdateRequest) {

        ReservationEntity reservationEntity = searchForReservation(reservationUpdateRequest);
        FlightEntity flightEntity = reservationEntity.getFlightEntity();

        if(!reservationEntity.getSeatEntity().getSeatNumber().equals(reservationUpdateRequest.getSeatNumber())) {
            SeatEntity seatEntity = seatService.getSeatEntity(flightEntity, reservationUpdateRequest.getSeatNumber());

            seatService.checkIfSeatIsAvailable(seatEntity);

            reservationEntity.getSeatEntity().setAvailable(true);
            seatService.saveSeat(reservationEntity.getSeatEntity());

            seatEntity.setAvailable(false);
            seatService.saveSeat(seatEntity);

            reservationEntity.setSeatEntity(seatEntity);
        }

        PassengerEntity passengerEntity = passengerService.getPassengerEntity(reservationUpdateRequest.getPassengerDto());

        if(!reservationUpdateRequest.getPassengerDto().getPhoneNumber().equals(passengerEntity.getPhoneNumber())) {

            passengerEntity.setPhoneNumber(reservationUpdateRequest.getPassengerDto().getPhoneNumber());
        }

        if(!reservationUpdateRequest.getPassengerDto().getEmail().equals(passengerEntity.getEmail())) {
            passengerEntity.setEmail(reservationUpdateRequest.getPassengerDto().getEmail());
        }

        passengerService.savePassenger(passengerEntity);

        return ReservationIdentifierResponse.builder()
                .data(RESERVATION_UPDATED_MESSAGE)
                .warnings(List.of())
                .errors(List.of())
                .build();
    }

    @Transactional
    public ReservationIdentifierResponse deleteReservation(ReservationIdentifierRequest reservationIdentifierRequest) {

        SeatEntity seatEntity = searchForReservation(reservationIdentifierRequest).getSeatEntity();
        seatEntity.setAvailable(true);

        reservationRepository.deleteByReservationNumber(reservationIdentifierRequest.getReservationNumber());
        return ReservationIdentifierResponse.builder()
                .data(RESERVATION_DELETED_MESSAGE)
                .warnings(List.of())
                .errors(List.of())
                .build();
    }

    public ReservationReadResponse getReservation(ReservationIdentifierRequest reservationIdentifierRequest) {

        return ReservationReadResponse.builder()
                .data(mapToReservationDto(searchForReservation(reservationIdentifierRequest)))
                .warnings(List.of())
                .errors(List.of())
                .build();
    }

    public List<ReservationReadResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToReservationDto)
                .map(reservationDto -> ReservationReadResponse.builder()
                        .data(reservationDto)
                        .warnings(List.of())
                        .errors(List.of())
                        .build())
                .toList();
    }

    private ReservationDto mapToReservationDto(ReservationEntity reservationEntity) {
        return ReservationDto.builder()
                .reservationNumber(reservationEntity.getReservationNumber())
                .flightDeparture(reservationEntity.getFlightEntity().getFlightDeparture().getAirportCity())
                .flightArrival(reservationEntity.getFlightEntity().getFlightArrival().getAirportCity())
                .flightDate(reservationEntity.getFlightEntity().getFlightDate())
                .flightDepartureTime(reservationEntity.getFlightEntity().getFlightDepartureTime())
                .seatNumber(reservationEntity.getSeatEntity().getSeatNumber())
                .passengerEmail(reservationEntity.getPassengerEntity().getEmail())
                .passengerPhoneNumber(reservationEntity.getPassengerEntity().getPhoneNumber())
                .build();
    }

    private ReservationEntity createReservationEntity(ReservationCreateRequest reservationCreateRequest) {

        passengerService.searchForPassenger(reservationCreateRequest.getPassengerDto());

        FlightEntity flightEntity = flightService.getFlightEntity(reservationCreateRequest);
        SeatEntity seatEntity = seatService.getSeatEntity(flightEntity, reservationCreateRequest.getSeatNumber());

        seatService.checkIfSeatIsAvailable(seatEntity);

        seatEntity.setAvailable(false);
        seatService.saveSeat(seatEntity);

        Long reservationNumber = reservationRepository.findMaxReservationNumber() + 1;

        return ReservationEntity.builder()
                .passengerEntity(passengerService.getPassengerEntity(reservationCreateRequest.getPassengerDto()))
                .flightEntity(flightEntity)
                .seatEntity(seatEntity)
                .reservationNumber(reservationNumber)
                .flightStatus(FlightStatus.SCHEDULED)
                .build();
    }

    private ReservationEntity searchForReservation(ReservationIdentifierRequest reservationIdentifierRequest) {
        return reservationRepository.findByReservationNumber(
                reservationIdentifierRequest.getReservationNumber())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(RESERVATION_NOT_FOUND_MESSAGE).code(1L).build());

    }

    private ReservationEntity searchForReservation(ReservationUpdateRequest reservationUpdateRequest) {
        return reservationRepository.findByReservationNumber(
                reservationUpdateRequest.getReservationNumber())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(RESERVATION_NOT_FOUND_MESSAGE).code(1L).build());
    }

}
