package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
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
  public void execute(RobotImp robotImp) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack rack = session.load(Rack.class, robotImp.getRackUUID());
      Robot robotP = session.load(Robot.class, robotImp.getId());

      MessageBroker.Instance().getGrid().getNode(rack.getX(), rack.getY()).updateOccupation(false);
      robotP.setRack(rack);
      rack.setStatus(Rack.Status.MOVING);
      rack.setX(robotImp.getX());
      rack.setY(robotImp.getY());
      rack.setZ(-10);
      MessageBroker.Instance().parentObject(robotImp.getId(), robotImp.getRackUUID());

      session.saveChanges();
      robotImp.taskDone(this);
    }
  }
}
