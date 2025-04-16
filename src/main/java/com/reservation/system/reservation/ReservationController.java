package com.reservation.system.reservation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<ReservationCreateResponse> createReservation(@RequestBody @Valid ReservationCreateRequest reservationCreateRequest) {
        return ResponseEntity.ok(reservationService.createReservation(reservationCreateRequest));
    }

    //TODO dodawanie rezerwacji
    //TODO modyfikacja rezerwacji - zmiana dnia wylotu?
    //TODO anulowanie (usuwanie) rezerwacji
    //TODO odczytywanie danych rezerwacji
    //TODO sprawdzenie czy wybrane miejsce nie zostało wcześniej zarezerwowane
    //TODO po utworzeniu rezerwacji wysłanie EMAIL! z informacjami o rezerwacji - numer rezerwacji, numer lotu oraz data wylotu
}
