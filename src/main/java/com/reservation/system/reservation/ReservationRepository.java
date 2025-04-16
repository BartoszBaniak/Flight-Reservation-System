package com.reservation.system.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT COALESCE(MAX(r.reservationNumber), 999L) FROM ReservationEntity r")
    Long findMaxReservationNumber();
}
