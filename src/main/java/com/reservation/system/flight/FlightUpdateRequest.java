package com.reservation.system.flight;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightUpdateRequest {

    private FlightIdentifierRequest flightUpdateRequestIdentifier;
    private FlightUpdateRequestDto flightUpdateRequestDto;
}
