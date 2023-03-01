package com.github.ellabailo.interconnectedflightsrestservice.model;

import java.util.List;

public class DaySchedule {
    private int day;

    private List<Flight> flights;

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
