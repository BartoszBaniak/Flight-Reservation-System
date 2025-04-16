package com.reservation.system.reservation;

import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportRepository;
import com.reservation.system.airport.AirportService;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flight.FlightCreateResponse;
import com.reservation.system.flight.FlightEntity;
import com.reservation.system.flight.FlightRepository;
import com.reservation.system.passenger.PassengerDto;
import com.reservation.system.passenger.PassengerEntity;
import com.reservation.system.passenger.PassengerRepository;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    public static final String RESERVATION_NOT_FOUND_MESSAGE = "Reservation not found";
    public static final String RESERVATION_CREATED_MESSAGE = "Reservation created successfully";
    public static final String FLIGHT_NOT_FOUND_MESSAGE = "Flight not found";
    public static final String SEAT_RESERVED_MESSAGE = "Seat is already reserved.";

    private final ReservationRepository reservationRepository;
    private final AirportService airportService;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final PassengerRepository passengerRepository;

    @Transactional
    public ReservationCreateResponse createReservation(ReservationCreateRequest reservationCreateRequest) {

        reservationRepository.save(createReservationEntity(reservationCreateRequest));

        return ReservationCreateResponse.builder().data(RESERVATION_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();


    }

    private ReservationEntity createReservationEntity(ReservationCreateRequest reservationCreateRequest) {

        PassengerEntity passengerEntity = getPassengerEntity(reservationCreateRequest.getPassengerDto());
        FlightEntity flightEntity = getFlightEntity(reservationCreateRequest);
        SeatEntity seatEntity = getSeatEntity(flightEntity, reservationCreateRequest.getSeatNumber());

        if(!seatEntity.isAvailable()) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(SEAT_RESERVED_MESSAGE).code(1L).build();
        }

        seatEntity.setAvailable(false);
        seatRepository.save(seatEntity);

        Long reservationNumber = reservationRepository.findMaxReservationNumber() + 1;

        return ReservationEntity.builder()
                .passengerEntity(passengerEntity)
                .flightEntity(flightEntity)
                .seatEntity(seatEntity)
                .reservationNumber(reservationNumber)
                .flightStatus(FlightStatus.SCHEDULED)
                .build();

    }

    private PassengerEntity getPassengerEntity(PassengerDto passengerDto) {
        if(passengerDto.getEmail() == null && passengerDto.getPhoneNumber() == null) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message("Passenger don't exists").code(1L).build();

        }

        return passengerRepository.getPassengerEntityByEmailAndPhoneNumber(
                passengerDto.getEmail(),
                passengerDto.getPhoneNumber());
    }

    private FlightEntity getFlightEntity(ReservationCreateRequest reservationCreateRequest) {

        AirportEntity flightDeparture = airportService.getAirportByCity(reservationCreateRequest.getFlightDeparture());
        AirportEntity flightArrival = airportService.getAirportByCity(reservationCreateRequest.getFlightArrival());

        return flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightDeparture,
                        flightArrival,
                        reservationCreateRequest.getFlightDate(),
                        reservationCreateRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(1L).build());

    }

    private SeatEntity getSeatEntity(FlightEntity flightEntity, String seatNumber) {

        return seatRepository.findByFlightEntityAndSeatNumber(flightEntity, seatNumber)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

}
