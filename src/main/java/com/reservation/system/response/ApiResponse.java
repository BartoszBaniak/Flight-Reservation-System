package com.reservation.system.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private String message;
    private T data;
}
