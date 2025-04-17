package com.reservation.system.exceptions;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    FLIGHT_NOT_FOUND(1001),
    PASSENGER_NOT_FOUND(1002),
    RESERVATION_NOT_FOUND(1003),
    SEAT_NOT_FOUND(1004),
    AIRPORT_NOT_FOUND(1005),

    FLIGHT_EXIST(2001),
    PASSENGER_EXIST(2002),
    SEAT_RESERVED(2003),

    FLIGHT_CREATED(3001),
    FLIGHT_UPDATED(3002),
    FLIGHT_DELETED(3003),

    PASSENGER_CREATED(3004),
    PASSENGER_UPDATED(3005),
    PASSENGER_DELETED(3006),

    RESERVATION_CREATED(3007),
    RESERVATION_UPDATED(3008),
    RESERVATION_DELETED(3009);

    private final long errorCode;

    ErrorEnum(long errorCode) {
        this.errorCode = errorCode;
    }

}
