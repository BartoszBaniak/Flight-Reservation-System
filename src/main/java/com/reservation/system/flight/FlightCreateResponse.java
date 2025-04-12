package com.reservation.system.flight;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FlightCreateResponse {

    private FlightEntity data;
    private List<String> errors;
    private List<String> warnings;

}
