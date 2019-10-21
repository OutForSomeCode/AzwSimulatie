package com.nhlstenden.amazonsimulatie.models;

public interface RobotTaskStrategy {
  void execute(Robot robot);
  Node getDestination();
}
