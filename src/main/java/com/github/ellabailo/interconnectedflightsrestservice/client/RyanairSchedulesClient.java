package com.github.ellabailo.interconnectedflightsrestservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.ellabailo.interconnectedflightsrestservice.model.Schedule;

import reactor.core.publisher.Mono;

@Service
public class RyanairSchedulesClient {
    private String baseUrl = "https://services-api.ryanair.com";
    private String schedulesUri = "/timtbl/3/schedules/%s/%s/years/%s/months/%s";

    private final WebClient webClient;

    @Autowired
    public RyanairSchedulesClient() {
        this.webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build();
    }

    public Mono<Schedule> getSchedule(String departure, String arrival, String year, String month) {
        String formattedSchedulesUri = getFormattedSchedulesUri(departure, arrival, year, month);
        return webClient.get()
        .uri(formattedSchedulesUri)
        .retrieve()
        .bodyToMono(Schedule.class);
    }

    private String getFormattedSchedulesUri (String departure, String arrival, String year, String month) { return String.format(schedulesUri, departure, arrival, year, month );};
};
