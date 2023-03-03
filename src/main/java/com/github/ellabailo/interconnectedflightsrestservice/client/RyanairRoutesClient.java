package com.github.ellabailo.interconnectedflightsrestservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.ellabailo.interconnectedflightsrestservice.model.Route;
import reactor.core.publisher.Flux;

@Service
public class RyanairRoutesClient implements RoutesClient {

    private String baseUrl = "https://services-api.ryanair.com";
    private String routesUri = "/locate/3/routes";

    private final WebClient webClient;

    @Autowired
    public RyanairRoutesClient() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<Route> getRoutes() {
        return webClient.get()
                .uri(routesUri)
                .retrieve()
                .bodyToFlux(Route.class);
    }
}
