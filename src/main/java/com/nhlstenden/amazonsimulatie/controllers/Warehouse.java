package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.RobotLogic;
import com.nhlstenden.amazonsimulatie.models.RobotTaskStrategy;
//robot callbacks for when there done
public interface Warehouse {
  void robotFinishedTask(RobotLogic robotLogic, RobotTaskStrategy task);
}
