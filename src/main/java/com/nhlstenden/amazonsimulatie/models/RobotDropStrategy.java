package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import net.ravendb.client.documents.session.IDocumentSession;

public class RobotDropStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotDropStrategy(Node destination) {
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

      robotP.setRackUUID(null);
      rack.setStatus(Rack.RackStatus.STORED);
      rack.updatePosition(robot.getX(), robot.getY(), 0);
      MessageBroker.Instance().unparentObject(robot.getUUID(), robot.getRackUUID());
      MessageBroker.Instance().updateObject(rack);

      session.saveChanges();
      robot.taskDone();
    }
  }
}
