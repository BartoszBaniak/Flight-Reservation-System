package com.reservation.system.flight;

import com.reservation.system.dictionaries.flightNumber.FlightNumber;
import com.reservation.system.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public ApiResponse<FlightCreateResponse> createFlight(FlightCreateRequest flightCreateRequest) {

        if (flightRepository.existsByFlightNumberAndFlightDateAndFlightDepartureTime(
                flightCreateRequest.getFlightNumber(),
                flightCreateRequest.getFlightDate(),
                flightCreateRequest.getFlightDepartureTime())) {

            FlightCreateResponse flightCreateResponse = new FlightCreateResponse(
                    null,
                    List.of("Flight already exists"),
                    List.of("Please check the flight details")
            );

            return new ApiResponse<FlightCreateResponse>(LocalDateTime.now(), "Flight already exists", flightCreateResponse);
        }

        Duration duration = Duration.between(flightCreateRequest.getFlightDepartureTime(), flightCreateRequest.getFlightArrivalTime());
        long durationInMinutes = duration.toMinutes();
        int hours = (int) (durationInMinutes / 60);
        int minutes = (int) (durationInMinutes % 60);

        String flightDuration = String.format("%02d:%02d", hours, minutes);


        FlightEntity flightEntity = new FlightEntity(
                flightCreateRequest.getFlightDeparture(),
                flightCreateRequest.getFlightArrival(),
                flightCreateRequest.getFlightDepartureTime(),
                flightCreateRequest.getFlightArrivalTime(),
                flightDuration,
                flightCreateRequest.getFlightDate(),
                flightCreateRequest.getFlightNumber(),
                flightCreateRequest.getFlightType(),
                flightCreateRequest.getFlightSeatsNumber()
        );

        FlightEntity savedFlight = flightRepository.save(flightEntity);

        FlightCreateResponse flightCreateResponse = new FlightCreateResponse(
                savedFlight,
                List.of(),
                List.of()
        );

        return new ApiResponse<FlightCreateResponse>(LocalDateTime.now(), "Flight created successfully", flightCreateResponse);
    }

    public FlightEntity getFlightByFlightNumber(FlightNumber flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("Flight number" + flightNumber + "not found"));
    }

    public FlightEntity getFlightById(int id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    public List<FlightEntity> getAllFlights() {
        return flightRepository.findAll();
    }

    public FlightEntity updateFlight(int id, FlightEntity flightEntity) {
        FlightEntity existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        existingFlight.setFlightDeparture(flightEntity.getFlightDeparture());
        existingFlight.setFlightArrival(flightEntity.getFlightArrival());
        existingFlight.setFlightDepartureTime(flightEntity.getFlightDepartureTime());
        existingFlight.setFlightDuration(flightEntity.getFlightDuration());
        existingFlight.setFlightDate(flightEntity.getFlightDate());
        existingFlight.setFlightNumber(flightEntity.getFlightNumber());
        existingFlight.setFlightType(flightEntity.getFlightType());
        existingFlight.setFlightSeatsNumber(flightEntity.getFlightSeatsNumber());

        return flightRepository.save(flightEntity);
    }

    public void deleteFlight(int id) {
        if(!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found");
        }
        flightRepository.deleteById(id);
    }
}
