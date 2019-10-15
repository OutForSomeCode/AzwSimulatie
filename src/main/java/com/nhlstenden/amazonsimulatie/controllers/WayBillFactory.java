package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Data;
import com.nhlstenden.amazonsimulatie.models.Rack;
import com.nhlstenden.amazonsimulatie.models.Truck;
import com.nhlstenden.amazonsimulatie.models.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WayBillFactory {
  private World world;
  private Random random = new Random();
  private List<Rack> racks = new ArrayList<>();
  private int time = 600;

  public WayBillFactory(World world) {

  }

  public void update() {
    time--;
    if (time >= 0) {
      requestResources();
      time = random.nextInt();
    }
  }

  public Truck requestResources() {
    List<Rack> cargo = new ArrayList<>();
    for (int i = 0; i < Data.cargoSize; i++) {
      cargo.add(world.getUnusedRack(Data.cargoType[random.nextInt(Data.cargoType.length)]));
    }
    return new Truck(cargo);
  }

  public Truck requestResources(String type, int amount) {
    if (amount < Data.cargoSize)
      requestResources(type, (amount - Data.cargoSize));
    List<Rack> cargo = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      cargo.add(world.getUnusedRack(type));
    }
    return new Truck(cargo);
  }
}
