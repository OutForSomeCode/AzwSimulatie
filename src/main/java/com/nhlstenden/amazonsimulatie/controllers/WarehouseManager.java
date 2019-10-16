package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Data;
import com.nhlstenden.amazonsimulatie.models.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

public class WarehouseManager implements Resource {
  private int loadingBay = 0;
  private World world;
  private int time = 0;
  private boolean[] loadingBayInUse = new boolean[Data.modules]; // for testing
  private Random r = new Random();

  public WarehouseManager(World world) {
    this.world = world;

    for (int i = 0; i < (6 * Data.modules); i++) {
      world.addRobot(0, 0);
    }

    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          world.addWall((29 - x), y);
        }
      }
    }
  }

  public void update() {
  }

  private Node rackDropLocation() {
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 != 1 && y % 6 != 2 && y % 6 != 3 && y % 6 != 4) {
        continue;
      }
      for (int x : Data.rackPositionsX) {
        if (world.getGrid().getNode(x, y).getOccupation() != null)
          continue;
        world.getGrid().getNode(x, y).updateOccupation(new Wall());
        return world.getGrid().getNode(x, y);
      }
    }
    return null;
  }

  @Override
  public void RequestResource(String resource, int amount) {
    Deque<Rack> temp = new ArrayDeque<>();
    for (int i = 0; i < 10; i++) {
      temp.addFirst(world.getUnusedRack("kaas"));
    }
    Waybill dump = new Waybill(temp);
    WaybillResolver.Instance().StoreResource(dump);
  }

  @Override
  public void StoreResource(Waybill waybill) {
    Deque<Rack> cargo = waybill.getRacks();
    loadingBay++;
    if (loadingBay > 9) loadingBay = 0;

    for (int y = (loadingBay * 6); y < ((loadingBay * 6) + 6); y++) {
      if (y % 6 != 2 && y % 6 != 3)
        continue;
      for (int x = 0; x < 5; x++) {
        if (cargo.isEmpty())
          continue;

        Rack rack = cargo.remove();
        world.addRack(rack.getType(), 29 - x, y);

        Robot robot = world.findIdleRobot();
        if (robot == null)
          continue;
        ArrayList<RobotTask> t = new ArrayList<>();
        t.add(new RobotTask(world.getGrid().getNode((29 - x), y), RobotTask.Task.PICKUP));
        t.add(new RobotTask(rackDropLocation(), RobotTask.Task.DROP));
        t.add(new RobotTask(world.getGrid().getNode(0, r.nextInt((6 * Data.modules))), RobotTask.Task.PARK));
        robot.assignTask(t);
      }
    }
  }
}
