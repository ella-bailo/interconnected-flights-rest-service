package com.github.ellabailo.interconnectedflightsrestservice.model;

import java.util.List;

public class Schedule {
    private int month;

    private List<DaySchedule> days;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<DaySchedule> getDays() {
        return days;
    }

    public void setDays( List<DaySchedule> days) {
        this.days = days;
    }
}
