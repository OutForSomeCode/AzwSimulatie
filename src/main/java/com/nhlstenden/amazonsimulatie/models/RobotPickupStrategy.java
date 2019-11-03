package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
import net.ravendb.client.documents.session.IDocumentSession;

/*
 * pickup strategy for robots
 */
public class RobotPickupStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotPickupStrategy(Node destination) {
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

      robotLogic.getGrid().getNode(rack.getX(), rack.getY()).setOccupied(false);
      robotP.setRack(rack);

      // update carried rack status
      rack.setStatus(Rack.Status.MOVING);
      rack.setX(robotLogic.getX());
      rack.setY(robotLogic.getY());
      rack.setZ(-10);

      // connect rack to the robot
      MessageBroker.Instance().parentObject(robotLogic.getId(), robotLogic.getRackUUID());

      session.saveChanges();

      // callback that the task has been executed
      robotLogic.taskDone(this);
    }
  }
}
