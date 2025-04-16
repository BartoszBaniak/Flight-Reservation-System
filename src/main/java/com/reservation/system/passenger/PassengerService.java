package com.reservation.system.passenger;

import com.reservation.system.exceptions.InternalBusinessException;
import com.reservation.system.reservation.ReservationEntity;
import com.reservation.system.seat.SeatEntity;
import com.reservation.system.seat.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class PassengerService {

    public static final String PASSENGER_EXIST_MESSAGE = "Passenger already exists";
    public static final String PASSENGER_CREATED_MESSAGE = "Passenger created successfully.";
    public static final String PASSENGER_NOT_FOUND_MESSAGE = "Passenger not found.";
    public static final String PASSENGER_DELETED_MESSAGE = "Passenger deleted successfully";
    public static final String PASSENGER_UPDATE_MESSAGE = "Passenger updated successfully";

    private final PassengerRepository passengerRepository;

    private final SeatService seatService;

    @Transactional
    public PassengerCreateResponse createPassenger(PassengerDto passengerRequestDto) {
        if(checkIfPassengerExists(passengerRequestDto)) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(PASSENGER_EXIST_MESSAGE).code(1L).build();
        }
        passengerRepository.save(createPassengerEntity(passengerRequestDto));

        return PassengerCreateResponse.builder().data(PASSENGER_CREATED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public PassengerIdentifierResponse deletePassenger(PassengerIdentifierRequest passengerIdentifierRequest) {

        searchForPassenger(passengerIdentifierRequest);
        List<ReservationEntity> reservationEntities = searchForPassenger(passengerIdentifierRequest).getReservations();

        for(ReservationEntity reservationEntity : reservationEntities) {
            SeatEntity seatEntity = reservationEntity.getSeatEntity();
            seatEntity.setAvailable(true);
            seatService.saveSeat(seatEntity);
        }

        passengerRepository.deleteByEmailAndPhoneNumber(
                passengerIdentifierRequest.getEmail(),
                passengerIdentifierRequest.getPhoneNumber()
        );

        return PassengerIdentifierResponse.builder().data(PASSENGER_DELETED_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    @Transactional
    public PassengerIdentifierResponse updatePassenger(PassengerUpdateRequest passengerUpdateRequest) {

        PassengerIdentifierRequest passengerIdentifierRequest = passengerUpdateRequest.getPassengerIdentifierRequest();
        PassengerDto passengerRequestDto = passengerUpdateRequest.getPassengerRequestDto();

        PassengerEntity existingPassenger = passengerRepository.findByEmailAndPhoneNumber(
                passengerIdentifierRequest.getEmail(),
                passengerIdentifierRequest.getPhoneNumber())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(PASSENGER_NOT_FOUND_MESSAGE).code(2L).build());

        updatePassengerEntity(existingPassenger, passengerRequestDto);

        return PassengerIdentifierResponse.builder().data(PASSENGER_UPDATE_MESSAGE).warnings(List.of()).errors(List.of()).build();
    }

    public PassengerReadResponse getPassenger(PassengerIdentifierRequest passengerIdentifierRequest) {

        PassengerEntity passengerEntity = searchForPassenger(passengerIdentifierRequest);
        PassengerDto passengerDto = mapToPassengerDto(passengerEntity);

        return PassengerReadResponse.builder()
                .data(passengerDto)
                .warnings(List.of())
                .errors(List.of())
                .build();
    }

    public List<PassengerReadResponse> getAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(this::mapToPassengerDto)
                .map(passengerDto -> PassengerReadResponse.builder()
                        .data(passengerDto)
                        .warnings(List.of())
                        .errors(List.of())
                        .build())
                .toList();
    }

    public PassengerEntity getPassengerEntity(PassengerDto passengerDto) {
        if(StringUtils.isEmpty(passengerDto.getEmail()) && StringUtils.isEmpty(passengerDto.getPhoneNumber())) {
            throw InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(PASSENGER_NOT_FOUND_MESSAGE).code(1L).build();
        }

        return passengerRepository.getPassengerEntityByEmailAndPhoneNumber(
                passengerDto.getEmail(),
                passengerDto.getPhoneNumber());
    }


    public void searchForPassenger(PassengerDto passengerRequestDto) {
        passengerRepository.findByEmailAndPhoneNumber(passengerRequestDto.getEmail(), passengerRequestDto.getPhoneNumber())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(PASSENGER_NOT_FOUND_MESSAGE).code(1L).build());
    }

    public void savePassenger(PassengerEntity passengerEntity) {
        passengerRepository.save(passengerEntity);
    }

    private void updatePassengerEntity(PassengerEntity existingPassenger, PassengerDto passengerRequestDto) {
        existingPassenger.setFirstName(passengerRequestDto.getFirstName());
        existingPassenger.setLastName(passengerRequestDto.getLastName());
        existingPassenger.setEmail(passengerRequestDto.getEmail());
        existingPassenger.setPhoneNumber(passengerRequestDto.getPhoneNumber());
    }

    private PassengerDto mapToPassengerDto(PassengerEntity passengerEntity) {
        return PassengerDto.builder()
                .firstName(passengerEntity.getFirstName())
                .lastName(passengerEntity.getLastName())
                .email(passengerEntity.getEmail())
                .phoneNumber(passengerEntity.getPhoneNumber())
                .build();
    }

    private PassengerEntity searchForPassenger(PassengerIdentifierRequest passengerIdentifierRequest) {
        return passengerRepository.findByEmailAndPhoneNumber(
                passengerIdentifierRequest.getEmail(),
                passengerIdentifierRequest.getPhoneNumber())
                .orElseThrow(() -> InternalBusinessException.builder().type(HttpStatus.BAD_REQUEST).message(PASSENGER_NOT_FOUND_MESSAGE).code(1L).build());
    }

    private boolean checkIfPassengerExists(PassengerDto passengerRequestDto) {
        return passengerRepository.existsByEmailAndPhoneNumber(
                passengerRequestDto.getEmail(),
                passengerRequestDto.getPhoneNumber());

    }

    private PassengerEntity createPassengerEntity(PassengerDto passengerRequestDto) {
        return PassengerEntity.builder()
                .firstName(passengerRequestDto.getFirstName())
                .lastName(passengerRequestDto.getLastName())
                .email(passengerRequestDto.getEmail())
                .phoneNumber(passengerRequestDto.getPhoneNumber())
                .build();
    }
}