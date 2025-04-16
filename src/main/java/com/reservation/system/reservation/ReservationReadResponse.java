package com.reservation.system.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationReadResponse {

    private ReservationDto data;
    private List<String> errors;
    private List<String> warnings;
}
