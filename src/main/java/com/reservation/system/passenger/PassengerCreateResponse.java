package com.reservation.system.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PassengerCreateResponse {

    private String data;
    private List<String> errors;
    private List<String> warnings;
}
