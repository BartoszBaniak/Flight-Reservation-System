package com.reservation.system.config;

import com.reservation.system.airport.AirportEntity;
import com.reservation.system.airport.AirportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAirports(AirportRepository airportRepository) {
        return args -> {
            List<AirportEntity> airportEntities = List.of(
                    new AirportEntity("LHR", "London"),
                    new AirportEntity("CDG", "Paris"),
                    new AirportEntity("AMS", "Amsterdam"),
                    new AirportEntity("FRA", "Frankfurt"),
                    new AirportEntity("IST", "Istanbul"),
                    new AirportEntity("MAD", "Madrid"),
                    new AirportEntity("BCN", "Barcelona"),
                    new AirportEntity("FCO", "Rome"),
                    new AirportEntity("CPH", "Copenhagen"),
                    new AirportEntity("ZRH", "Zurich")
            );

            airportEntities.forEach(airport -> {
                if(!airportRepository.existsAirportEntityByAirportCode(airport.getAirportCode())) {
                    airportRepository.save(airport);
                }
            });

            System.out.println("wygenerowano baze danych");
        };
    }
}