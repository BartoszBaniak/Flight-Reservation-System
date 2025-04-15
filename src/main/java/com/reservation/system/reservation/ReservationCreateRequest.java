package com.reservation.system.reservation;

import com.reservation.system.passenger.PassengerDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationCreateRequest {

    private PassengerDto passengerDto;
    private String flightDeparture;
    private String flightArrival;
    private LocalDate flightDate;
    private LocalTime flightDepartureTime;
    private String seatNumber;
}
