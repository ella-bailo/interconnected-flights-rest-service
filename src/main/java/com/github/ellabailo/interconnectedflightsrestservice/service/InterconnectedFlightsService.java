package com.github.ellabailo.interconnectedflightsrestservice.service;

import com.github.ellabailo.interconnectedflightsrestservice.dto.InterconnectedFlightDto;
import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;

public interface InterconnectedFlightsService {
  Flux<InterconnectedFlightDto> getFlights(
    Airport departure,
    Airport arrival,
    LocalDateTime departureDateTime,
    LocalDateTime arrivalDateTime
  );
}
