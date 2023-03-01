package com.github.ellabailo.interconnectedflightsrestservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Route {

  private final Airport airportFrom;

  private final Airport airportTo;

  @JsonCreator
  public Route(
    @JsonProperty("airportFrom") Airport from,
    @JsonProperty("airportTo") Airport to
  ) {
    this.airportFrom = from;
    this.airportTo = to;
  }

  private String connectingAirport;

  private Boolean newRoute;

  private Boolean seasonalRoute;

  private String operator;

  private String group;

  public Airport getAirportFrom() {
    return airportFrom;
  }

  public Airport getAirportTo() {
    return airportTo;
  }

  public String getConnectingAirport() {
    return connectingAirport;
  }

  public void setConnectingAirport(String connectingAirport) {
    this.connectingAirport = connectingAirport;
  }

  public Boolean getNewRoute() {
    return newRoute;
  }

  public void setNewRoute(Boolean newRoute) {
    this.newRoute = newRoute;
  }

  public Boolean getSeasonalRoute() {
    return seasonalRoute;
  }

  public void setSeasonalRoute(Boolean seasonalRoute) {
    this.seasonalRoute = seasonalRoute;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }
}
