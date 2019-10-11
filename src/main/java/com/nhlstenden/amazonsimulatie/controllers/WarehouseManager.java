package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Node;
import com.nhlstenden.amazonsimulatie.models.Robot;
import com.nhlstenden.amazonsimulatie.models.RobotTask;
import com.nhlstenden.amazonsimulatie.models.World;

import java.util.ArrayList;
import java.util.Random;

public class WarehouseManager {
  private World world;
  private Random r = new Random();

  public WarehouseManager(World world) {
    this.world = world;
    for (int i = 0; i < 3; i++) {
      world.addRobot(r.nextInt(5), r.nextInt(5));
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
