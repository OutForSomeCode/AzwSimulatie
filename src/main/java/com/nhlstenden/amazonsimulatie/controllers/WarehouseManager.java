package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Data;
import com.nhlstenden.amazonsimulatie.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WarehouseManager {
  private World world;
  private int time = 0;
  private Random r = new Random();

  public WarehouseManager(World world) {
    this.world = world;
    for (int i = 0; i < (2 * Data.modules); i++) {
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
    time++;
    if (time >= 50) {
      unloadTruck(r.nextInt(3));
      List<Rack> racks = world.getUsedRacks();
      if (racks.size() > 150)
        for (Rack o : racks) {
          o.putInPool();
          world.getGrid().getNode(o.x,o.y).updateOccupation(null);
        }
      time = 1;
    }
  }

  public void loadTruck(int loadingBay) {

  }

  public void unloadTruck(int loadingBay) {
    for (int y = (loadingBay * 6); y < ((loadingBay * 6) + 6); y++) {
      if (y % 6 == 2 || y % 6 == 3) {
        for (int x = 0; x < 1; x++) {
          world.addRack("kaas", (29 - x), y);
          Robot robot = world.findIdleRobot();
          if (robot != null) {
            ArrayList<RobotTask> t = new ArrayList<>();
            t.add(new RobotTask(world.getGrid().getNode((29 - x), y), RobotTask.Task.PICKUP));
            t.add(new RobotTask(rackDropLocation(), RobotTask.Task.DROP));
            t.add(new RobotTask(world.getGrid().getNode(0, r.nextInt((6 * Data.modules))), RobotTask.Task.PARK));
            robot.assignTask(t);
          }
        }
      }
    }
  }

  private Node rackDropLocation() {
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 1 || y % 6 == 2 || y % 6 == 3 || y % 6 == 4) {
        for (int x : Data.rackPositionsX) {
          if (world.getGrid().getNode(x, y).getOccupation() == null) {
            world.getGrid().getNode(x, y).updateOccupation(new Wall());
            return world.getGrid().getNode(x, y);
          }
        }
      }
    }
    return rackDropLocation();
  }
}
