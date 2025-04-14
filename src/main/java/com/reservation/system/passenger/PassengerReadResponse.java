package com.reservation.system.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PassengerReadResponse {

    private PassengerDto data;
    private List<String> errors;
    private List<String> warnings;
}
