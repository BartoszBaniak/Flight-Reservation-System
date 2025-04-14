package com.reservation.system.passenger;

import com.reservation.system.flight.FlightUpdateRequest;
import com.reservation.system.flight.FlightUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/create")
    public ResponseEntity<PassengerCreateResponse> createPassenger(@RequestBody @Valid PassengerRequestDto passengerRequestDto) {
        return ResponseEntity.ok(passengerService.createPassenger(passengerRequestDto));
    }

    @GetMapping("/search")
    public ResponseEntity<PassengerReadResponse> getPassenger(@RequestBody PassengerIdentifierRequest passengerIdentifierRequest) {
        return ResponseEntity.ok(passengerService.getPassenger(passengerIdentifierRequest));
    }

    @GetMapping("/search-all")
    public ResponseEntity<List<PassengerReadResponse>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<PassengerIdentifierResponse> deletePassenger(@RequestBody PassengerIdentifierRequest passengerIdentifierRequest) {
        return ResponseEntity.ok(passengerService.deletePassenger(passengerIdentifierRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<PassengerIdentifierResponse> updatePassenger(@RequestBody @Valid PassengerUpdateRequest passengerUpdateRequest) {
        return ResponseEntity.ok(passengerService.updatePassenger(passengerUpdateRequest));
    }




    //TODO dodawanie pasażera (przy tworzeniu rezerwacji, walidacja po email czy już istnieje?) - jest ale oddzielne, nie przy tworzeniu rejestracji
    //TODO modyfikacja pasażerów
    //TODO anulowanie (usuwanie) pasażerów - jest
    //TODO odczytywanie danych pasażera - jest dla pojedynczego i dla wszystkich

}