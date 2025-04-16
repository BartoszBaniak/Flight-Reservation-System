package com.reservation.system.reservation;

import com.reservation.system.passenger.PassengerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationUpdateRequest {

    private Long reservationNumber;
    private String seatNumber;
    private PassengerDto passengerDto;
}
