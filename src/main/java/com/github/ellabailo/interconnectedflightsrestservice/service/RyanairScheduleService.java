package com.github.ellabailo.interconnectedflightsrestservice.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ellabailo.interconnectedflightsrestservice.client.RyanairSchedulesClient;
import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.model.DaySchedule;
import com.github.ellabailo.interconnectedflightsrestservice.model.Flight;
import com.github.ellabailo.interconnectedflightsrestservice.model.Schedule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RyanairScheduleService implements ScheduleService {

    @Autowired
    private RyanairSchedulesClient ryanairschedulesClient;

    public Flux<Flight> getDaysScheduledFlights(Airport departureAirport, Airport arrivalAirport, LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        String parsedMonth = String.valueOf(month);
        String parsedYear = String.valueOf(year);
        String parsedDepartureAirport = String.valueOf(departureAirport);
        String parsedArrivalAirport = String.valueOf(arrivalAirport);

        Mono<Schedule> scheduleMono = ryanairschedulesClient.getSchedule(parsedDepartureAirport, parsedArrivalAirport,
                parsedYear, parsedMonth);

        int dayOfMonth = localDate.getDayOfMonth();

        return scheduleMono.map(schedule -> {
            List<DaySchedule> daySchedules = schedule.getDays();

            List<Flight> flights = new ArrayList<Flight>();
            for (DaySchedule daySchedule : daySchedules) {
                if (daySchedule.getDay() == dayOfMonth) {
                    flights = daySchedule.getFlights();
                }
            }
            return flights;
        }).flatMapIterable(list -> list);
    }
}
