package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Destination;
import com.nhlstenden.amazonsimulatie.models.Rack;
import com.nhlstenden.amazonsimulatie.models.Resource;
import com.nhlstenden.amazonsimulatie.models.Waybill;
import com.nhlstenden.amazonsimulatie.models.World;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class MelkFactory implements Resource {
  boolean flipflop;
  private int time = 0;
  private Random r = new Random();

  public void update() {
    time++;
    if (time < 50)
      return;
    flipflop = !flipflop;
    if (flipflop) {
      // Store goods
      Deque<Rack> temp = new ArrayDeque<>();
      for (int i = 0; i < r.nextInt(9) + 1; i++) {
        temp.addFirst(World.Instance().getUnusedRack("kaas"));
      }
      Waybill dump = new Waybill(temp, Destination.WAREHOUSE);
      WaybillResolver.Instance().StoreResource(dump);
    } else {
      // Request goods
      WaybillResolver.Instance().RequestResource("kaas", 10);
    }
    time = 0;
  }

  @Override
  public void RequestResource(String resource, int amount) {

  }

  @Override
  public void StoreResource(Waybill waybill) {
    for (Rack r : waybill.getRacks()) {
      r.putInPool();
    }
  }
}
