package com.reservation.system.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReservationCreateResponse {

    private String data;
    private List<String> errors;
    private List<String> warnings;
}
