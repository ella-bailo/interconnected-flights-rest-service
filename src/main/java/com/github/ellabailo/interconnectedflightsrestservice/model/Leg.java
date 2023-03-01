package com.github.ellabailo.interconnectedflightsrestservice.model;

public class Leg {
    private Airport departureAirport;
    private Airport arrivalAirport;

    public Leg(Airport departureAirport, Airport arrivalAirport ) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }
}
