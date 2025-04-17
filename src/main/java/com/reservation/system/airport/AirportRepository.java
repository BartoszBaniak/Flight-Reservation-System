package com.reservation.system.airport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, Integer> {

    AirportEntity findByAirportCode(String airportCode);

    AirportEntity findByAirportCity(String airportCity);

    boolean existsAirportEntityByAirportCode(String airportCode);

}
