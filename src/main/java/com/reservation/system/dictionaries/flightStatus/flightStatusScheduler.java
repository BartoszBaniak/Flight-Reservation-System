package com.reservation.system.dictionaries.flightStatus;

import com.reservation.system.flight.FlightReadResponse;
import com.reservation.system.flight.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@EnableScheduling
@AllArgsConstructor
public class flightStatusScheduler {

    private final FlightService flightService;

    @Scheduled(cron = "*/10 * * * * *")
    public void updateFlightStatus() {

        LocalDateTime now = LocalDateTime.now();

        List<FlightReadResponse> flightEntities = flightService.getAllFlights();
        for (FlightReadResponse flightEntity : flightEntities) {

            LocalDateTime flightDateTime = LocalDateTime.of(flightEntity.getData().getFlightDate(), flightEntity.getData().getFlightDepartureTime());

            if (flightDateTime.isBefore(now) && flightEntity.getData().getFlightStatus() != FlightStatus.ARRIVED) {
                flightService.updateFlightStatus(
                        flightEntity.getData().getFlightDepartureTime(),
                        flightEntity.getData().getFlightDate(),
                        flightEntity.getData().getFlightDeparture().getAirportCode(),
                        flightEntity.getData().getFlightArrival().getAirportCode(),
                        FlightStatus.ARRIVED);

            } else if (flightDateTime.isAfter(now) && flightEntity.getData().getFlightStatus() != FlightStatus.SCHEDULED) {
                flightService.updateFlightStatus(
                        flightEntity.getData().getFlightDepartureTime(),
                        flightEntity.getData().getFlightDate(),
                        flightEntity.getData().getFlightDeparture().getAirportCode(),
                        flightEntity.getData().getFlightArrival().getAirportCode(),
                        FlightStatus.SCHEDULED);

            } else if (flightDateTime.isBefore(now) && flightEntity.getData().getFlightStatus() != FlightStatus.DEPARTED) {
                flightService.updateFlightStatus(flightEntity.getData().getFlightDepartureTime(),
                        flightEntity.getData().getFlightDate(),
                        flightEntity.getData().getFlightDeparture().getAirportCode(),
                        flightEntity.getData().getFlightArrival().getAirportCode(),
                        FlightStatus.DEPARTED);
            }
        }
    }
}
