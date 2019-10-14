package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.*;

import java.util.ArrayList;
import java.util.Random;

public class WarehouseManager {
  private World world;
  private int modules = 4;
  private int[] rackPositions = {3, 4, 7, 8, 11, 12, 15, 16, 19, 20};
  private Random r = new Random();

  public WarehouseManager(World world) {
    this.world = world;
    for (int i = 0; i < 16; i++) {
      world.addRobot(3, 1);
    }

    for (int y = 0; y < (modules * 6); y++) {
      if (y % 6 == 1 || y % 6 == 2 || y % 6 == 3 || y % 6 == 4) {
        for (int r : rackPositions) {
          world.addRack(Rack.Type.KAAS, r, y);
        }
      }
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          world.addWall((29 - x), y);
        }
      }
    }
  }

  public void update() {
    Robot robot = world.findIdleRobot();
    if (robot != null) {
      ArrayList<RobotTask> t = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        Node randomNode = world.getGrid().RandomNode();
        t.add(new RobotTask(randomNode, RobotTask.Task.PICKUP));
      }
      robot.assignTask(t);
    }
  }
}
