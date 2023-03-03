package com.github.ellabailo.interconnectedflightsrestservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonPropertyOrder(
  {
    "departureAirport",
    "arrivalAirport",
    "departureDateTime",
    "arrivalDateTime",
  }
)
public class InterconnectedFlightDto implements Serializable {

  private final int stops;
  private final List<LegDto> legs;

  @JsonCreator
  public InterconnectedFlightDto(
    @JsonProperty("stops") int stops,
    @JsonProperty("legs") List<LegDto> legs
  ) {
    this.stops = stops;
    this.legs = legs;
  }

  public int getStops() {
    return stops;
  }

  public List<LegDto> getLegs() {
    return legs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    InterconnectedFlightDto that = (InterconnectedFlightDto) o;
    return stops == that.stops && Objects.equals(legs, that.legs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stops, legs);
  }

  @Override
  public String toString() {
    return (
      "InterconnectedFlightDto{" + "stops=" + stops + ", legs=" + legs + '}'
    );
  }
}
