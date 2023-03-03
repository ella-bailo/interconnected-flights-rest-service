package com.github.ellabailo.interconnectedflightsrestservice.service;

import com.github.ellabailo.interconnectedflightsrestservice.client.RyanairRoutesClient;
import com.github.ellabailo.interconnectedflightsrestservice.model.Airport;
import com.github.ellabailo.interconnectedflightsrestservice.model.Route;
import java.util.ArrayList;
import java.util.List;
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
public class RyanairRoutesService implements RoutesService {

  @Autowired
  private RyanairRoutesClient ryanairRoutesClient;

  public Flux<List<Route>> findRoutes(
    Airport departureAirport,
    Airport arrivalAirport,
    int maxLegs
  ) {
    if (
      departureAirport.equals(arrivalAirport) || maxLegs < 1
    ) return Flux.empty();

    Flux<Route> routes = ryanairRoutesClient.getRoutes();

    Mono<List<Route>> collectedRoutes = routes.collectList();

    Mono<DirectedGraph<Airport, DefaultEdge>> routesGraph =
      this.getRoutesGraph(collectedRoutes);

    Mono<List<List<Route>>> routesList = routesGraph.map(graph -> {
      AllDirectedPaths<Airport, DefaultEdge> directedPathFinder = new AllDirectedPaths<>(
        graph
      );

      if (
        !graph.vertexSet().contains(departureAirport) ||
        !graph.vertexSet().contains(arrivalAirport)
      ) throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "departure or arrival airport not found"
      );

      List<GraphPath<Airport, DefaultEdge>> directedPaths = directedPathFinder.getAllPaths(
        departureAirport,
        arrivalAirport,
        true,
        maxLegs
      );

      List<List<Route>> allRoutes = directedPaths
        .stream()
        .map(directedPath -> {
          List<Airport> airports = directedPath.getVertexList();
          List<Route> legs = new ArrayList<>();

          for (int i = 0; i < airports.size() - 1; i++) {
            Route leg = new Route(
              airports.get(i),
              airports.get(i + 1),
              "RYANAIR",
              "null"
            );
            legs.add(leg);
          }
          return legs;
        })
        .collect(Collectors.toList());

      return allRoutes;
    });
    return Flux.merge(routesList).flatMapIterable(list -> list);
  }

  private Mono<DirectedGraph<Airport, DefaultEdge>> getRoutesGraph(
    Mono<List<Route>> collectedRoutes
  ) {
    return collectedRoutes.map(collectedRoute -> {
      DirectedGraph<Airport, DefaultEdge> graph = new DefaultDirectedGraph<Airport, DefaultEdge>(
        DefaultEdge.class
      );
      collectedRoute.forEach(route -> {
        graph.addVertex(route.getFrom());
        graph.addVertex(route.getTo());
        graph.addEdge(route.getFrom(), route.getTo());
      });
      return graph;
    });
  }
}
