package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
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
  public void execute(RobotLogic robotLogic) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack rack = session.load(Rack.class, robotLogic.getRackUUID());
      Robot robotP = session.load(Robot.class, robotLogic.getId());

      MessageBroker.Instance().getGrid().getNode(rack.getX(), rack.getY()).setOccupied(false);
      robotP.setRack(rack);
      rack.setStatus(Rack.Status.MOVING);
      rack.setX(robotLogic.getX());
      rack.setY(robotLogic.getY());
      rack.setZ(-10);
      MessageBroker.Instance().parentObject(robotLogic.getId(), robotLogic.getRackUUID());

      session.saveChanges();
      robotLogic.taskDone(this);
    }
  }
}
