package com.reservation.system.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class InternalBusinessException extends RuntimeException {
    private final Long code;
    private final HttpStatus type;
    private final String message;
}
