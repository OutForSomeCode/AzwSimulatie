package com.nhlstenden.amazonsimulatie.models;

public class RobotDropStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotDropStrategy(Node destination) {
    this.destination = destination;
  }

  @Override
  public void update(Robot robot) {

  }
}
