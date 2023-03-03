package com.github.ellabailo.interconnectedflightsrestservice.service;

import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.model.Route;
import java.util.List;
import reactor.core.publisher.Flux;

public interface RoutesService {
  Flux<List<Route>> findRoutes(Airport departure, Airport arrival, int maxLegs);
}
