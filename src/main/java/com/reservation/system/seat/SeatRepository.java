package com.reservation.system.seat;

import com.reservation.system.flight.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<SeatEntity, Integer> {

    SeatEntity findByFlightEntityAndSeatNumber(
            FlightEntity flightEntity,
            String seatNumber
    );

    void deleteByFlightEntity(FlightEntity flightEntity);

}
