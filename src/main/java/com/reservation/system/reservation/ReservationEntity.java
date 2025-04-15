package com.reservation.system.reservation;


import com.reservation.system.flight.FlightEntity;
import com.reservation.system.passenger.PassengerEntity;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import com.reservation.system.seat.SeatEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @Column(nullable = false)
    private Long reservationNumber; //to do random generator

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerEntity passengerEntity;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatEntity seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;
}
