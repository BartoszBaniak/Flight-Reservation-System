package com.reservation.system.flight;

import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    public ApiResponse<FlightCreateResponse> createFlight(@RequestBody FlightCreateRequest flightCreateRequest) {

        return flightService.createFlight(flightCreateRequest);
    }


    /*
    @GetMapping("/{id}")
    public ResponseEntity<FlightEntity> getFlightById(@PathVariable int id) {
        FlightEntity flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightEntity> getFlightByFlightNumber(@PathVariable FlightNumber flightNumber) {
        FlightEntity flight = flightService.getFlightByFlightNumber(flightNumber);
        return ResponseEntity.ok(flight);
    }

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

    //TODO dodawanie lotów - tworzenie nowego lotu
    //TODO modyfikacja lotów - zmiana godziny wylotu
    //TODO anulowanie (usuwanie) lotów
    //TODO odczytywanie danych lotów - informacja skąd dokąd, o której godzinie wylatuje
}
