package com.github.ellabailo.interconnectedflightsrestservice.service;

import com.github.ellabailo.interconnectedflightsrestservice.dto.InterconnectedFlightDto;
import com.github.ellabailo.interconnectedflightsrestservice.dto.LegDto;
import com.github.ellabailo.interconnectedflightsrestservice.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RyanairInterconnectedFlightsService
        implements InterconnectedFlightsService {

    private final int maxLegs = 2;
    private final int minTransferMinutes = 120;

    @Autowired
    RyanairRoutesService ryanairRoutesService;

    @Autowired
    RyanairScheduleService ryanairScheduleService;

    public Flux<InterconnectedFlightDto> getFlights(
            Airport departure,
            Airport arrival,
            LocalDateTime departureDateTime,
            LocalDateTime arrivalDateTime) {
        boolean departureAndArrivalIsSameDay = departureDateTime.getDayOfYear() == arrivalDateTime.getDayOfYear() &&
                arrivalDateTime.compareTo(arrivalDateTime) == 0;

        if (!departureAndArrivalIsSameDay)
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Departure and arrival must be on the same day");

        if (departure.equals(arrival))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Arrival airport cannot be the same as departure airport");

        if (departureDateTime.isAfter(arrivalDateTime))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Arrival date time must be after Departure date time");

        LocalDate localDate = LocalDate.from(departureDateTime);
        LocalDateTime minDepartureTime = departureDateTime;
        LocalDateTime maxArrivalTime = arrivalDateTime;

        Flux<List<Route>> allRoutes = ryanairRoutesService.findRoutes(
                departure,
                arrival,
                maxLegs);
        Flux<InterconnectedFlightDto> allFlightCombinations = Flux.merge(
                allRoutes.map(route -> {
                    Flux<InterconnectedFlightDto> interconnectedFlights = Flux
                            .merge(
                                    this.getAllInterconnectedFlightsForRoute(
                                            route,
                                            localDate,
                                            minDepartureTime,
                                            maxArrivalTime))
                            .flatMapIterable(list -> list);
                    return interconnectedFlights;
                }));
        return allFlightCombinations;
    }

    private Mono<List<InterconnectedFlightDto>> getAllInterconnectedFlightsForRoute(
            List<Route> route,
            LocalDate localDate,
            LocalDateTime minDepartureTime,
            LocalDateTime maxArrivalTime) {
        Airport departureAirport = route.get(0).getFrom();
        Airport destinationAirport = route.get(route.size() - 1).getTo();
        Mono<List<LegDto>> allFlightsInRouteMono = this.getAllPossibleFlightsInRoute(route, localDate).collectList();

        Mono<List<InterconnectedFlightDto>> interconnectedFlightsMono = allFlightsInRouteMono.map(allFlightsInRoute -> {
            Set<LegDto> sourceVertices = new HashSet<>();
            Set<LegDto> targetVertices = new HashSet<>();
            DirectedGraph<LegDto, DefaultEdge> graph = new DefaultDirectedGraph<LegDto, DefaultEdge>(
                    DefaultEdge.class);

            for (LegDto flight : allFlightsInRoute) {
                boolean isDepartureAirport = flight.getDepartureAirport() == departureAirport;
                if (isDepartureAirport && flight.getDepartureDateTime().isAfter(minDepartureTime)) {
                    sourceVertices.add(flight);
                }
                boolean isArrivalAirport = flight.getArrivalAirport() == destinationAirport;
                if (isArrivalAirport && flight.getArrivalDateTime().isBefore(maxArrivalTime)) {
                    targetVertices.add(flight);
                }
                graph.addVertex(flight);
                Set<LegDto> allVertices = graph.vertexSet();

                for (LegDto vertex : allVertices) {
                    boolean vertexIsPreviousLeg = flight.getDepartureAirport() == vertex.getArrivalAirport();
                    if (vertexIsPreviousLeg) {
                        boolean timingValid = isValidFlight(
                                vertex.getDepartureDateTime(),
                                vertex.getArrivalDateTime(),
                                flight.getDepartureDateTime(),
                                flight.getArrivalDateTime(),
                                minDepartureTime,
                                maxArrivalTime);
                        if (timingValid) {
                            graph.addEdge(vertex, flight);
                        }
                    }
                }
            }
            AllDirectedPaths<LegDto, DefaultEdge> directedPathFinder = new AllDirectedPaths<>(
                    graph);
            List<GraphPath<LegDto, DefaultEdge>> directedPaths = directedPathFinder.getAllPaths(
                    sourceVertices,
                    targetVertices,
                    true,
                    null);
            return directedPaths
                    .stream()
                    .map(directedPath -> {
                        List<LegDto> legs = directedPath.getVertexList();
                        InterconnectedFlightDto interconnectedFlight = new InterconnectedFlightDto(
                                legs.size() - 1,
                                legs);
                        return interconnectedFlight;
                    })
                    .collect(Collectors.toList());
        });
        return interconnectedFlightsMono;
    }

    private Flux<LegDto> getAllPossibleFlightsInRoute(
            List<Route> routes,
            LocalDate localDate) {
        return Flux.merge(
                Flux.fromIterable(
                        routes
                                .stream()
                                .map(route -> {
                                    Airport departureAirport = route.getFrom();
                                    Airport arrivalAirport = route.getTo();

                                    Flux<Flight> flights = ryanairScheduleService.getDaysScheduledFlights(
                                            departureAirport,
                                            arrivalAirport,
                                            localDate);

                                    return flights.map(flight -> {
                                        LocalDateTime departureDateTime = LocalDateTime.of(
                                                localDate,
                                                flight.getDepartureTime());
                                        LocalDateTime arrivalDateTime = LocalDateTime.of(
                                                localDate,
                                                flight.getArrivalTime());

                                        return new LegDto(
                                                departureAirport,
                                                arrivalAirport,
                                                departureDateTime,
                                                arrivalDateTime);
                                    });
                                })
                                .collect(Collectors.toList())));
    }

    private boolean isValidFlight(
            LocalDateTime previousLegDepartureTime,
            LocalDateTime previousLegArrivalTime,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            LocalDateTime minDepartureTime,
            LocalDateTime maxArrivalTime) {
        boolean previousFlightValid = previousLegArrivalTime.isAfter(previousLegDepartureTime);
        boolean flightValid = arrivalTime.isAfter(departureTime);

        boolean departureTimeValid = departureTime.isAfter(previousLegArrivalTime.plusMinutes(minTransferMinutes)) && departureTime.isAfter(minDepartureTime);
        boolean arrivalTimeValid = arrivalTime.isBefore(maxArrivalTime);

        return previousFlightValid && flightValid  && departureTimeValid && arrivalTimeValid;
    }
}
