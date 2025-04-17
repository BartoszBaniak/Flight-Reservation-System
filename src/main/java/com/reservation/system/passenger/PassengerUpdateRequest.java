package com.reservation.system.passenger;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PassengerUpdateRequest {

    private PassengerIdentifierRequest passengerIdentifierRequest;
    private PassengerDto passengerRequestDto;

}
