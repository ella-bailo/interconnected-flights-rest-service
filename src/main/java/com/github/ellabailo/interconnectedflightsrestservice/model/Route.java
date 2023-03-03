package com.github.ellabailo.interconnectedflightsrestservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;

public class Route implements Serializable {

  private final Airport from;
  private final Airport to;
  private final String operator;
  private final String connectingAirport;

  @JsonCreator
  public Route(
    @JsonProperty("airportFrom") Airport from,
    @JsonProperty("airportTo") Airport to,
    @JsonProperty("operator") String operator,
    @JsonProperty("connectingAirport") String connectingAirport
  ) {
    this.from = from;
    this.to = to;
    this.operator = operator;
    this.connectingAirport = connectingAirport;
  }

  public Airport getFrom() {
    return from;
  }

  public Airport getTo() {
    return to;
  }

  public String getOperator() {
    return operator;
  }

  public String getConnectingAirport() {
    return connectingAirport;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Route route = (Route) o;
    return (
      Objects.equals(from, route.from) &&
      Objects.equals(to, route.to) &&
      Objects.equals(operator, route.operator) &&
      Objects.equals(connectingAirport, route.connectingAirport)
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, operator, connectingAirport);
  }

  @Override
  public String toString() {
    return (
      "Route{" +
      "from='" +
      from +
      '\'' +
      ", to='" +
      to +
      '\'' +
      ", connectingAirport='" +
      connectingAirport +
      '\'' +
      ", operator='" +
      operator +
      '\'' +
      '}'
    );
  }
}
