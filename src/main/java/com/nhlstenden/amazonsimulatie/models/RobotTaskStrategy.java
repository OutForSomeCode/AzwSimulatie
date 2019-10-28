package com.nhlstenden.amazonsimulatie.models;

public interface RobotTaskStrategy {
  void execute(RobotLogic robotLogic);

  Node getDestination();
}
