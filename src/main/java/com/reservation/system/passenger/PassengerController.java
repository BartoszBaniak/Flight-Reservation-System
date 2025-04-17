package com.reservation.system.passenger;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
@AllArgsConstructor
@Validated
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/create")
    public ResponseEntity<PassengerCreateResponse> createPassenger(@RequestBody @Valid PassengerDto passengerRequestDto) {
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

}