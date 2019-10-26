package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
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
  public void execute(RobotImp robotImp) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack rack = session.load(Rack.class, robotImp.getRackUUID());
      Robot robotP = session.load(Robot.class, robotImp.getId());

      robotP.setRack(null);
      robotP.setStatus(Robot.Status.IDLE);

      rack.setStatus(Rack.Status.STORED);
      rack.setX(robotImp.getX());
      rack.setY(robotImp.getY());
      rack.setZ(0);

      MessageBroker.Instance().unparentObject(robotImp.getId(), robotImp.getRackUUID());
      MessageBroker.Instance().updateObject(rack);

      session.saveChanges();
      robotImp.taskDone(this);
    }
  }
}
