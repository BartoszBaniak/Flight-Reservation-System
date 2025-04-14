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
    public ResponseEntity<FlightReadResponse> getFlight(@RequestBody FlightIdentifierRequest flightIdentifierRequest) {
        return ResponseEntity.ok(flightService.getFlight(flightIdentifierRequest));
    }

    @GetMapping("/search-all")
    public ResponseEntity<List<FlightReadResponse>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FlightIdentifierResponse> deleteFlight(@RequestBody FlightIdentifierRequest flightIdentifierRequest) {
        return ResponseEntity.ok(flightService.deleteFlight(flightIdentifierRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<FlightIdentifierResponse> updateFlight(@RequestBody FlightUpdateRequest flightUpdateRequest) {
        return ResponseEntity.ok(flightService.updateFlight(flightUpdateRequest));
    }

    //TODO numery lotów i lotniska do oddzielnych entity - nie słowniki!
    //TODO dodawanie lotów - tworzenie nowego lotu ------- trzeba dodac uwzględnienie zmiany dnia i dodac automatyczne przypisywanie numeru lotu
}
