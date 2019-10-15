package com.nhlstenden.amazonsimulatie.models;

import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Truck implements Updatable{
  private UUID uuid;
  private Deque<Rack> cargo;
  private int loadingBay;

  public Truck(Deque<Rack> rack) {
    cargo = rack;
    this.uuid = UUID.randomUUID();
  }

  public int getLoadingBay() {
    return loadingBay;
  }

  public void setLoadingBay(int loadingBay) {
    this.loadingBay = loadingBay;
  }

  public Deque<Rack> getCargo() {
    return cargo;
  }

  @Override
  public boolean update() {
    return false;
  }
}
