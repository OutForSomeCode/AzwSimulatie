package com.nhlstenden.amazonsimulatie.models;

public class RobotParkStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotParkStrategy(Node destination) {
    this.destination = destination;
  }

  @Override
  public void update(Robot robot) {
  }
}
