package com.reservation.system.flight;

import jakarta.validation.Valid;
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
    public ResponseEntity<FlightCreateResponse> createFlight(@RequestBody @Valid FlightCreateRequest flightCreateRequest) {
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
    public ResponseEntity<FlightIdentifierResponse> updateFlight(@RequestBody @Valid FlightUpdateRequest flightUpdateRequest) {
        return ResponseEntity.ok(flightService.updateFlight(flightUpdateRequest));
    }

}
