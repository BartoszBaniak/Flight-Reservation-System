package com.reservation.system.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append(fieldError.getDefaultMessage()).append(" ");
        }
        return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
    }
}
