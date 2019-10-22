package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Robot;
import com.nhlstenden.amazonsimulatie.models.RobotTaskStrategy;

public interface Warehouse {
  void robotFinishedTask(Robot robot, RobotTaskStrategy task);
}
