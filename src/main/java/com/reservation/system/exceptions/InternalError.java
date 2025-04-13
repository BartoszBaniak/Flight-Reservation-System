package com.reservation.system.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class InternalError {
    private final Long code;
    private final String type;
    private final String message;
}
