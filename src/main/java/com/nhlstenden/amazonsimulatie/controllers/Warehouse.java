package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.RobotImp;
import com.nhlstenden.amazonsimulatie.models.RobotTaskStrategy;

public interface Warehouse {
  void robotFinishedTask(RobotImp robotImp, RobotTaskStrategy task);
}
