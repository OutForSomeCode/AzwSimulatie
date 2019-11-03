package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
import net.ravendb.client.documents.session.IDocumentSession;

/*
 * park strategy for robots
 */
public class RobotParkStrategy implements RobotTaskStrategy {
  private Node destination;

  public RobotParkStrategy(Node destination) {
    this.destination = destination;
  }

  public Node getDestination() {
    return destination;
  }

  // what the robot needs to do when this task gets executed
  @Override
  public void execute(RobotLogic robotLogic) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Robot robotP = session.load(Robot.class, robotLogic.getId());

      // robot is put to idle, so the system knows it can be used again for other tasks
      robotP.setStatus(Robot.Status.IDLE);

      session.saveChanges();

      // callback that the task has been executed
      robotLogic.taskDone(this);
    }
  }
}
