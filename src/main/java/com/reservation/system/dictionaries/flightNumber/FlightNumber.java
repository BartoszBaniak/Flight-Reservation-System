package com.reservation.system.dictionaries.flightNumber;

import com.reservation.system.dictionaries.airport.Airport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.reservation.system.dictionaries.airport.Airport.*;

@AllArgsConstructor
@Getter
public enum FlightNumber {
    LO101(WAW, LHR),
    LO102(WAW, CDG),
    LO103(WAW, FRA),
    LO104(WAW, AMS),
    LO105(WAW, DXB),
    LO106(WAW, JFK),
    LO107(WAW, SIN),

    LO108(KRK, LHR),
    LO109(KRK, CDG),
    LO110(KRK, FRA),
    LO111(KRK, AMS),
    LO112(KRK, DXB),
    LO113(KRK, JFK),
    LO114(KRK, SIN),

    LO115(GDN, LHR),
    LO116(GDN, CDG),
    LO117(GDN, FRA),
    LO118(GDN, AMS),
    LO119(GDN, DXB),
    LO120(GDN, JFK),
    LO121(GDN, SIN),

    LO201(LHR, WAW),
    LO202(CDG, WAW),
    LO203(FRA, WAW),
    LO204(AMS, WAW),
    LO205(DXB, WAW),
    LO206(JFK, WAW),
    LO207(SIN, WAW),

    LO208(LHR, KRK),
    LO209(CDG, KRK),
    LO210(FRA, KRK),
    LO211(AMS, KRK),
    LO212(DXB, KRK),
    LO213(JFK, KRK),
    LO214(SIN, KRK),

    LO215(LHR, GDN),
    LO216(CDG, GDN),
    LO217(FRA, GDN),
    LO218(AMS, GDN),
    LO219(DXB, GDN),
    LO220(JFK, GDN),
    LO221(SIN, GDN);

    private final Airport airportDeparture;
    private final Airport airportArrival;


}
