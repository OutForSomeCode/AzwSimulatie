package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.base.Destination;

import java.util.Deque;
import java.util.UUID;

public class Waybill{
  private UUID uuid;
  private Destination destination;
  private Deque<String> racks;

  public Waybill(Deque<String> rack) {
    racks = rack;
    this.uuid = UUID.randomUUID();
  }

  public Waybill(Deque<String> rack, Destination destination) {
    this(rack);
    this.destination = destination;
  }

  public Deque<String> getRacks() {
    return racks;
  }

  public Destination getDestination() {
    return destination;
  }

  public void setDestination(Destination destination) {
    this.destination = destination;
  }
}
