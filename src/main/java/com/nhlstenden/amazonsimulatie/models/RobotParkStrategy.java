package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import net.ravendb.client.documents.session.IDocumentSession;

public class RobotParkStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotParkStrategy(Node destination) {
    this.destination = destination;
  }

  public Node getDestination() {
    return destination;
  }

  @Override
  public void execute(Robot robot) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack rack = session.load(Rack.class, robot.getRackUUID());
      RobotPOJO robotP = session.load(RobotPOJO.class, robot.getUUID());

      robotP.setStatus(Robot.RobotStatus.IDLE);

      session.saveChanges();
      robot.taskDone();
    }
  }
}
