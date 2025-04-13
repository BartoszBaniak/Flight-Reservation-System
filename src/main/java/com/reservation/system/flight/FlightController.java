package com.reservation.system.flight;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/flight")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<FlightCreateResponse> createFlight(@RequestBody FlightCreateRequest flightCreateRequest) {
        return ResponseEntity.ok(flightService.createFlight(flightCreateRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<FlightReadResponse> getFlight(@RequestBody FlightReadDeleteRequest flightReadDeleteRequest) {
        return ResponseEntity.ok(flightService.getFlight(flightReadDeleteRequest));
    }

    @GetMapping("/search-all")
    public ResponseEntity<List<FlightReadResponse>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FlightDeleteResponse> deleteFlight(@RequestBody FlightReadDeleteRequest flightReadDeleteRequest) {
        return ResponseEntity.ok(flightService.deleteFlight(flightReadDeleteRequest));
    }

//    @GetMapping("/{flightNumber}")
//    public ResponseEntity<FlightEntity> getFlightByFlightNumber(@PathVariable FlightNumber flightNumber) {
//        FlightEntity flight = flightService.getFlightByFlightNumber(flightNumber);
//        return ResponseEntity.ok(flight);
//    }

    /*

    @GetMapping("/all")
    public ResponseEntity<List<FlightEntity>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FlightEntity> updateFlight(@PathVariable int id, FlightEntity flightEntity) {
        FlightEntity updatedFlight = flightService.updateFlight(id, flightEntity);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

     */
    //TODO numery lotów i lotniska do oddzielnych entity - nie słowniki!
    //TODO dodawanie lotów - tworzenie nowego lotu ------- powiedzmy ze jest v1, trzeba dodac uwzględnienie zmiany dnia i dodac automatyczne przypisywanie numeru lotu
    //TODO modyfikacja lotów - zmiana godziny wylotu
    //TODO anulowanie (usuwanie) lotów - jest i usuwa z bazy
    //TODO odczytywanie danych lotów - informacja skąd dokąd, o której godzinie wylatuje - jest po ID i ALL
}
