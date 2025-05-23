package com.reservation.system.passenger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity, Integer> {

    boolean existsByEmailAndPhoneNumber(
            String email,
            String phoneNumber
    );

    PassengerEntity getPassengerEntityByEmail(
            String passengerEmail
    );

    void deleteByEmail(String email);
}
