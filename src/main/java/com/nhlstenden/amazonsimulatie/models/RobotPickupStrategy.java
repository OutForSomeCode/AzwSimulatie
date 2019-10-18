package com.nhlstenden.amazonsimulatie.models;

public class RobotPickupStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotPickupStrategy(Node destination) {
    this.destination = destination;
  }

  @Override
  public void update(Robot robot) {
  }
}
