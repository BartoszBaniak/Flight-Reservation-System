package com.reservation.system.passenger;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerUpdateRequest {

    private PassengerIdentifierRequest passengerIdentifierRequest;
    private PassengerRequestDto passengerRequestDto;

}
