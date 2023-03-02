package com.github.ellabailo.interconnectedflightsrestservice.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule {
    private final int month;
    private final List<DaySchedule> days;

    @JsonCreator
    public Schedule(@JsonProperty("month") int month,
            @JsonProperty("days") List<DaySchedule> days) {
        this.month = month;
        this.days = days;
    }

    public int getMonth() {
        return month;
    }

    public List<DaySchedule> getDays() {
        return days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Schedule that = (Schedule) o;
        return month == that.month &&
                Objects.equals(days, that.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, days);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "month=" + month +
                ", days=" + days +
                '}';
    }
}
