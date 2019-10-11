package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.*;

import java.util.ArrayList;
import java.util.List;

public class WarehouseManager {
  private World world;

  public WarehouseManager(World world) {
    this.world = world;
    for (int i = 0; i < 8; i++) {
      world.addRobot();
    }
  }
  public void update(){
    Robot robot = world.findIdleRobot();
    if (robot != null){
      //todo random task
    }
  }
}
