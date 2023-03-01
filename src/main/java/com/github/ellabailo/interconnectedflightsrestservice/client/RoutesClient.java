package com.github.ellabailo.interconnectedflightsrestservice.client;


import com.github.ellabailo.interconnectedflightsrestservice.model.Route;

import reactor.core.publisher.Flux;

public interface RoutesClient {
    Flux<Route> getRoutes();
    
}
