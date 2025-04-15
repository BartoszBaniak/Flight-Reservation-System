package com.reservation.system.flight;

import com.reservation.system.airport.AirportDto;
import com.reservation.system.airport.AirportEntity;
import com.reservation.system.flightConnection.FlightConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Integer> {

    boolean existsByFlightConnectionAndFlightDateAndFlightDepartureTime(
            FlightConnectionEntity flightConnection,
            LocalDate flightDate,
            LocalTime flightDepartureTime
    );

    Optional<FlightEntity> findByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
            AirportEntity flightDeparture,
            AirportEntity flightArrival,
            LocalDate flightDate,
            LocalTime flightDepartureTime
    );

    void deleteByFlightDepartureAndFlightArrivalAndFlightDateAndFlightDepartureTime(
            AirportEntity flightDeparture,
            AirportEntity flightArrival,
            LocalDate flightDate,
            LocalTime flightDepartureTime
    );
}
