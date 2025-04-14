package com.reservation.system.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FlightIdentifierResponse {

    private String data;
    private List<String> errors;
    private List<String> warnings;
}
