package com.reservation.system.reservation;

import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportRepository;
import com.reservation.system.airport.AirportService;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flight.FlightCreateResponse;
import com.reservation.system.flight.FlightEntity;
import com.reservation.system.flight.FlightRepository;
import com.reservation.system.passenger.PassengerEntity;
import com.reservation.system.passenger.PassengerRepository;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    public static final String RESERVATION_NOT_FOUND_MESSAGE = "Reservation not found";
    public static final String RESERVATION_CREATED_MESSAGE = "Reservation created successfully";
    public static final String FLIGHT_NOT_FOUND_MESSAGE = "Flight not found";
    public static final String SEAT_RESERVED_MESSAGE = "Seat is already resereved.";

    private final ReservationRepository reservationRepository;
    private final AirportRepository airportRepository;
    private final AirportService airportService;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final PassengerRepository passengerRepository;

    @Transactional
    public ReservationCreateResponse createReservation(ReservationCreateRequest reservationCreateRequest) { //todo PIERWSZE CO DO SPRAWDZENIA


        AirportEntity flightDeparture = airportService.getAirportByCity(reservationCreateRequest.getFlightDeparture());
        AirportEntity flightArrival = airportService.getAirportByCity(reservationCreateRequest.getFlightArrival());

        FlightEntity flightEntity = flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightDeparture,
                        flightArrival,
                        reservationCreateRequest.getFlightDate(),
                        reservationCreateRequest.getFlightDepartureTime())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(FLIGHT_NOT_FOUND_MESSAGE).code(1L).build());


        Optional<SeatEntity> seatOptional = seatRepository.findByFlightEntityAndSeatNumber(flightEntity, reservationCreateRequest.getSeatNumber());

        SeatEntity seatEntity;

        if (seatOptional.isPresent()) {
            seatEntity = seatOptional.get();
            if (!seatEntity.isAvailable()) {
                throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(SEAT_RESERVED_MESSAGE).code(1L).build();
            }
        } else {
            seatEntity = new SeatEntity();
            seatEntity.setFlightEntity(flightEntity);
            seatEntity.setSeatNumber(reservationCreateRequest.getSeatNumber());
            seatEntity.setAvailable(true);
            seatRepository.save(seatEntity);
        }

        reservationRepository.save(createReservationEntity(reservationCreateRequest));

        return ReservationCreateResponse.builder().data(RESERVATION_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();


    }

    private ReservationEntity createReservationEntity(ReservationCreateRequest reservationCreateRequest) {

        PassengerEntity passengerEntity = passengerRepository.getPassengerEntityByEmailAndPhoneNumber(
                reservationCreateRequest.getPassengerDto().getEmail(),
                reservationCreateRequest.getPassengerDto().getPhoneNumber());

        AirportEntity flightDeparture = airportService.getAirportByCity(reservationCreateRequest.getFlightDeparture());
        AirportEntity flightArrival = airportService.getAirportByCity(reservationCreateRequest.getFlightArrival());

        FlightEntity flightEntity = flightRepository.findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
                        flightDeparture,
                        flightArrival,
                        reservationCreateRequest.getFlightDate(),
                        reservationCreateRequest.getFlightDepartureTime())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        SeatEntity seatEntity = seatRepository.findByFlightEntityAndSeatNumber(flightEntity, reservationCreateRequest.getSeatNumber())
                .orElseThrow(() -> new RuntimeException("Seat not found"));


        return ReservationEntity.builder()
                .passengerEntity(passengerEntity)
                .flightEntity(flightEntity)
                .seatNumber(seatEntity)
                .build();

    }

}
