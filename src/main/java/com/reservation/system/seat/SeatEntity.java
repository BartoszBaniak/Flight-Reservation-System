package com.reservation.system.seat;


import com.reservation.system.flight.FlightEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"seat_number", "flight_id"})
})
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatId;

    @Column(nullable = false)
    private String seatNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private FlightEntity flightEntity;

    @Column(nullable = false)
    private boolean isAvailable;

}
