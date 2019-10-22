package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Waybill {
  private String id;
  private Destination destination;
  private WaybillStatus status;
  @JsonProperty("racks")
  private List<Rack> racks;

  @JsonCreator
  public Waybill(@JsonProperty("id")
                   String id,
                 @JsonProperty("destination")
                   Destination destination,
                 @JsonProperty("status")
                   WaybillStatus status,
                 @JsonProperty("racks")
                   List<Rack> racks) {
    this.id = id;
    this.status = status;
    this.destination = destination;
    this.racks = racks;
  }

  public Waybill(String id) {
    this.id = id;
  }

  @JsonProperty("racks")
  public List<Rack> getRacks() {
    return racks;
  }

  @JsonProperty("racks")
  public void setRacks(List<Rack> racks) {
    this.racks = racks;
  }

  @JsonProperty("destination")
  public Destination getDestination() {
    return destination;
  }

  @JsonProperty("destination")
  public void setDestination(Destination destination) {
    this.destination = destination;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("status")
  public WaybillStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(WaybillStatus status) {
    this.status = status;
  }

  public enum WaybillStatus {
    UNRESOLVED,
    RESOLVING,
    RESOLVED
  }
}
