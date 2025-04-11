package com.reservation.system.passenger;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passenger")
@AllArgsConstructor
public class PassengerController {

    //TODO dodawanie pasażera (przy tworzeniu rezerwacji, walidacja po email czy już istnieje?)
    //TODO modyfikacja pasażerów
    //TODO anulowanie (usuwanie) pasażerów
    //TODO odczytywanie danych pasażera

}
