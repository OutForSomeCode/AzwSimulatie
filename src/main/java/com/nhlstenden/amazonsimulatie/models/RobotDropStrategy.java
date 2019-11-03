package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
import net.ravendb.client.documents.session.IDocumentSession;


/*
 * drop strategy for robots
 */
public class RobotDropStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotDropStrategy(Node destination) {
    this.destination = destination;
  }

  public Node getDestination() {
    return destination;
  }

  // what the robot needs to do when this task gets executed
  @Override
  public void execute(RobotLogic robotLogic) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack rack = session.load(Rack.class, robotLogic.getRackUUID());
      Robot robotP = session.load(Robot.class, robotLogic.getId());

      robotP.setRack(null);

      // update carried rack status
      rack.setStatus(Rack.Status.STORED);
      rack.setX(robotLogic.getX());
      rack.setY(robotLogic.getY());
      rack.setZ(0);
      rack.setWkt(DocumentStoreHolder.formatWtk(robotLogic.getX(), robotLogic.getY()));

      // disconnect rack from the robot
      MessageBroker.Instance().unparentObject(robotLogic.getId(), robotLogic.getRackUUID());
      MessageBroker.Instance().updateObject(rack);

      session.saveChanges();

      // callback that the task has been executed
      robotLogic.taskDone(this);
    }
  }
}
