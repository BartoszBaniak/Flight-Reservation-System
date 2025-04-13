package com.reservation.system.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final InternalBusinessExceptionTranslator internalBusinessExceptionTranslator;


    @ExceptionHandler(InternalBusinessException.class)
    public ResponseEntity<InternalError> handleInternalBusinessException(InternalBusinessException e) {
        return new ResponseEntity<>(
                internalBusinessExceptionTranslator.translate(e), e.getType());
    }
}
