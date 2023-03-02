package com.github.ellabailo.interconnectedflightsrestservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.utils.DateTimeSerializer;

public class LegDto implements Serializable {
    private final Airport departureAirport;
    private final Airport arrivalAirport;
    private final LocalDateTime departureDateTime;
    private final LocalDateTime arrivalDateTime;

    @JsonCreator
    public LegDto(Airport from,
            Airport to,
            LocalDateTime fromDateTime,
            LocalDateTime toDateTime) {

        this.departureAirport = from;
        this.arrivalAirport = to;
        this.departureDateTime = fromDateTime;
        this.arrivalDateTime = toDateTime;
    }

    @JsonIgnore
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    @JsonGetter("departureAirport")
    public String getDepartureAirportCode() {
        return departureAirport.getIataCode();
    }

    @JsonIgnore
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    @JsonGetter("arrivalAirport")
    public String getArrivalAirportCode() {
        return arrivalAirport.getIataCode();
    }

    @JsonGetter("departureDateTime")
    @JsonSerialize(using = DateTimeSerializer.class)
    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    @JsonGetter("arrivalDateTime")
    @JsonSerialize(using = DateTimeSerializer.class)
    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LegDto leg = (LegDto) o;
        return Objects.equals(departureAirport, leg.departureAirport) &&
                Objects.equals(arrivalAirport, leg.arrivalAirport) &&
                Objects.equals(departureDateTime, leg.departureDateTime) &&
                Objects.equals(arrivalDateTime, leg.arrivalDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureAirport, arrivalAirport, departureDateTime, arrivalDateTime);
    }

    @Override
    public String toString() {
        return "LegDto{" +
                "departureAirport=" + departureAirport +
                ", arrivalAirport=" + arrivalAirport +
                ", departureDateTime=" + departureDateTime +
                ", arrivalDateTime=" + arrivalDateTime +
                '}';
    }
}
