package com.reservation.system.reservation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@AllArgsConstructor
public class ReservationController {

    //TODO dodawanie rezerwacji
    //TODO modyfikacja rezerwacji - zmiana dnia wylotu?
    //TODO anulowanie (usuwanie) rezerwacji
    //TODO odczytywanie danych rezerwacji
    //TODO sprawdzenie czy wybrane miejsce nie zostało wcześniej zarezerwowane
    //TODO po utworzeniu rezerwacji wysłanie EMAIL! z informacjami o rezerwacji - numer rezerwacji, numer lotu oraz data wylotu
}
