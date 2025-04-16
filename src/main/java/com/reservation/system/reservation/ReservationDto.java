package com.reservation.system.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Builder
@AllArgsConstructor
public class ReservationDto {

    private Long reservationNumber;
    private String flightDeparture;
    private String flightArrival;
    private LocalDate flightDate;
    private LocalTime flightDepartureTime;
    private String seatNumber;
    private String passengerEmail;
    private String passengerPhoneNumber;
}
