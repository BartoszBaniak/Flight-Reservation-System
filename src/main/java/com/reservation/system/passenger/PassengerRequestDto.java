package com.reservation.system.passenger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}
