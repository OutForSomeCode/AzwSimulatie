package com.nhlstenden.amazonsimulatie.models;

public interface RobotTaskStrategy {
  void execute(RobotImp robotImp);

  Node getDestination();
}
