package com.reservation.system.reservation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<ReservationCreateResponse> createReservation(@RequestBody @Valid ReservationCreateRequest reservationCreateRequest) {
        return ResponseEntity.ok(reservationService.createReservation(reservationCreateRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<ReservationReadResponse> getReservation(@RequestBody ReservationIdentifierRequest reservationIdentifierRequest) {
        return ResponseEntity.ok(reservationService.getReservation(reservationIdentifierRequest));
    }

    @GetMapping("/search-all")
    public ResponseEntity<List<ReservationReadResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PutMapping("/update")
    public ResponseEntity<ReservationIdentifierResponse> updateReservation(@RequestBody @Valid ReservationUpdateRequest reservationUpdateRequest) {
        return ResponseEntity.ok(reservationService.updateReservation(reservationUpdateRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReservationIdentifierResponse> deleteReservation(@RequestBody ReservationIdentifierRequest reservationIdentifierRequest) {
        return ResponseEntity.ok(reservationService.deleteReservation(reservationIdentifierRequest));
    }

}
