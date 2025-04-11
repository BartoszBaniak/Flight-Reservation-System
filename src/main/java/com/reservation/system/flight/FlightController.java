package com.reservation.system.flight;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flight")
@AllArgsConstructor
public class FlightController {

    //TODO dodawanie lotów - tworzenie nowego lotu
    //TODO modyfikacja lotów - zmiana godziny wylotu
    //TODO anulowanie (usuwanie) lotów
    //TODO odczytywanie danych lotów - informacja skąd dokąd, o której godzinie wylatuje
}
