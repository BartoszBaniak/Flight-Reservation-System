package com.reservation.system.flightConnection;

import com.reservation.system.airport.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightConnectionRepository extends JpaRepository<FlightConnectionEntity, Integer> {

    Optional<FlightConnectionEntity> findByDepartureAirportAndArrivalAirport(
            AirportEntity departureAirport,
            AirportEntity arrivalAirport
    );

    Optional<FlightConnectionEntity> findTopByOrderByFlightNumberDesc();
}
