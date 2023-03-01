package com.github.ellabailo.interconnectedflightsrestservice.service;

import java.util.List;

import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.model.Leg;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoutesService {
    Flux<List<Leg>> findRoutes(Airport departure, Airport arrival, int maxLegs);
}
