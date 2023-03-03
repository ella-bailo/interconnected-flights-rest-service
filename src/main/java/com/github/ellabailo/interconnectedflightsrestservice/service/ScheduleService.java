package com.github.ellabailo.interconnectedflightsrestservice.service;

import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.model.Flight;
import java.time.LocalDate;
import reactor.core.publisher.Flux;

public interface ScheduleService {
  Flux<Flight> getDaysScheduledFlights(
    Airport departureAirport,
    Airport arrivalAirport,
    LocalDate localDate
  );
}
