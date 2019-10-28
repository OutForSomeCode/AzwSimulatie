package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.RobotLogic;
import com.nhlstenden.amazonsimulatie.models.RobotTaskStrategy;

public interface Warehouse {
  void robotFinishedTask(RobotLogic robotLogic, RobotTaskStrategy task);
}
