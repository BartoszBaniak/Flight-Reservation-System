package com.reservation.system.seat;

import com.reservation.system.exceptions.ErrorEnum;
import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.flight.FlightEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatService {

    public static final String SEAT_RESERVED_MESSAGE = "Seat is already reserved.";
    public static final String SEAT_NOT_FOUND_MESSAGE = "Seat not found";

    private final SeatRepository seatRepository;

    public void saveAllSeats(List<SeatEntity> seatEntities) {
        seatRepository.saveAll(seatEntities);
    }

    public void saveSeat(SeatEntity seatEntity) {
        seatRepository.save(seatEntity);
    }

    public SeatEntity getSeatEntity(FlightEntity flightEntity, String seatNumber) {

        SeatEntity seatEntity = seatRepository.findByFlightEntityAndSeatNumber(flightEntity, seatNumber);
        if (seatEntity == null) {
            throw InternalBusinessException.builder()
                    .type(HttpStatus.BAD_REQUEST)
                    .message(SEAT_NOT_FOUND_MESSAGE)
                    .code(ErrorEnum.SEAT_NOT_FOUND.getErrorCode())
                    .build();
        }
        return seatEntity;
    }

    public List<SeatEntity> generateAndGetAvailableSeats(int seatsNumber, FlightEntity flightEntity) {

        List<SeatEntity> seatEntities = new ArrayList<>();

        for (int i = 0; i < seatsNumber; i++) {
            int row = (i / 6) + 1;
            char seatLetter = (char) ('A' + (i % 6));
            String seatNumber = row + String.valueOf(seatLetter);

            seatEntities.add(SeatEntity.builder()
                    .flightEntity(flightEntity)
                    .seatNumber(seatNumber)
                    .isAvailable(true)
                    .build());
        }

        return seatEntities;
    }

    public void checkIfSeatIsAvailable(SeatEntity seatEntity) {
        if(!seatEntity.isAvailable()) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(SEAT_RESERVED_MESSAGE).code(ErrorEnum.SEAT_RESERVED.getErrorCode()).build();
        }
    }

    public void deleteSeatsByFlight(FlightEntity flightEntity) {
        seatRepository.deleteByFlightEntity(flightEntity);
    }

    @Transactional
    public void refreshSeatsForFlight(FlightEntity flightEntity, int seatsNumber) {
        seatRepository.deleteByFlightEntity(flightEntity);
        List<SeatEntity> seatEntities = generateAndGetAvailableSeats(seatsNumber, flightEntity);
        saveAllSeats(seatEntities);
    }

}
