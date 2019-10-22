package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import net.ravendb.client.documents.session.IDocumentSession;

public class RobotPickupStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotPickupStrategy(Node destination) {
    this.destination = destination;
  }

  public Node getDestination() {
    return destination;
  }


  @Override
  public void execute(Robot robot) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack rack = session.load(Rack.class, robot.getRackUUID());
      RobotPOJO robotP = session.load(RobotPOJO.class, robot.getId());

      robotP.setRack(rack);
      rack.setStatus(Rack.RackStatus.MOVING);
      rack.updatePosition(robot.getX(), robot.getY(), -10);
      MessageBroker.Instance().parentObject(robot.getId(), robot.getRackUUID());

      session.saveChanges();
      robot.taskDone(this);
    }
  }
}
