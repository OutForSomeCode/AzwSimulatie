package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.base.Destination;

import java.util.Deque;
import java.util.UUID;

public class Waybill implements Updatable{
  private UUID uuid;
  private Destination destination;
  private Deque<Rack> racks;

  public Waybill(Deque<Rack> rack) {
    racks = rack;
    this.uuid = UUID.randomUUID();
  }

  public Waybill(Deque<Rack> rack, Destination destination) {
    this(rack);
    this.destination = destination;
  }

  public Deque<Rack> getRacks() {
    return racks;
  }

  @Override
  public boolean update() {
    return false;
  }

  public Destination getDestination() {
    return destination;
  }

  public void setDestination(Destination destination) {
    this.destination = destination;
  }
}
