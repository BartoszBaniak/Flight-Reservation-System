package com.reservation.system.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FlightReadResponse {

    private FlightDto data;
    private List<String> errors;
    private List<String> warnings;
}
