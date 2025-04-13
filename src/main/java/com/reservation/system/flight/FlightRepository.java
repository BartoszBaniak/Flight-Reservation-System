package com.reservation.system.flight;

import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Integer> {
    boolean existsByFlightNumberAndFlightDateAndFlightDepartureTime(
            FlightNumber flightNumber,
            LocalDate flightDate,
            LocalTime flightDepartureTime
    );
}
