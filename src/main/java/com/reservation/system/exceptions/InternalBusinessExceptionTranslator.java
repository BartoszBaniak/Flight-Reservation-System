package com.reservation.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class InternalBusinessExceptionTranslator {

    public InternalError translate(Throwable throwable) {
        InternalBusinessException exception = (InternalBusinessException) throwable;

        return InternalError.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .type(exception.getType().name())
                .build();
    }
}
