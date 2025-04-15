package com.reservation.system.reservation;


import com.reservation.system.flight.FlightEntity;
import com.reservation.system.passenger.PassengerEntity;
import com.reservation.system.dictionaries.flightStatus.FlightStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @Column(nullable = false)
    private int reservationNumber; //to do random generator

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flightEntity;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerEntity passengerEntity;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String passengerFirstName;

    @Column(nullable = false)
    private String passengerLastName;

    @Column(nullable = false)
    private String passengerEmail;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;
}
