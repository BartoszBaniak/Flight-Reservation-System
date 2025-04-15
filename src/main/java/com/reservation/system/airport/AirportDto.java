package com.reservation.system.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AirportDto {

    private String airportCode;
    private String airportCity;

}
