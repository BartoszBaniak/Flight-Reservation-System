package com.reservation.system.passenger;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PassengerIdentifierRequest {

    private String email;

}
