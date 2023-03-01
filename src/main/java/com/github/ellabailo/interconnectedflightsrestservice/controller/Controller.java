package com.github.ellabailo.interconnectedflightsrestservice.controller;

import java.time.LocalDateTime;

import com.github.ellabailo.interconnectedflightsrestservice.dto.InterconnectedFlightDto;
import com.github.ellabailo.interconnectedflightsrestservice.service.RyanairInterconnectedFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.service.RyanairRoutesService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired 
    RyanairRoutesService ryanairRoutesService;

    @Autowired
    RyanairInterconnectedFlightsService ryanairInterconnectedFlightsService;

     @GetMapping("interconnections")
     public Flux<InterconnectedFlightDto> getFlights(@RequestParam String departure, @RequestParam String arrival, @RequestParam String departureDateTime, @RequestParam String arrivalDateTime ) {
         Airport departureAirport = new Airport(departure);
         Airport arrivalAirport = new Airport(arrival);
         LocalDateTime parsedDepartureDateTime = LocalDateTime.parse(departureDateTime);
         LocalDateTime parsedArrivalDateTime = LocalDateTime.parse(arrivalDateTime);
         return ryanairInterconnectedFlightsService.getFlights(departureAirport, arrivalAirport, parsedDepartureDateTime, parsedArrivalDateTime);
     }
}
