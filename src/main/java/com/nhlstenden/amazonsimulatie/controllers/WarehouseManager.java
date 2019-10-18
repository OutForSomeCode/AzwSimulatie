package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Data;
import com.nhlstenden.amazonsimulatie.base.Destination;
import com.nhlstenden.amazonsimulatie.models.*;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.*;

import static com.nhlstenden.amazonsimulatie.base.Data.truckpost;

public class WarehouseManager implements Resource {
  private int loadingBay = 0;
  private int time = 0;
  private HashMap<String, Robot> robots = new HashMap<>();
  private boolean[] loadingBayInUse = new boolean[Data.modules]; // for testing
  private Random rand = new Random();

  public WarehouseManager() {
    int rAmount = Data.modules * 4;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      rAmount -= session.query(Robot.class).count();
    }

    if (rAmount > 0)
      try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
        for (int i = 0; i < rAmount; i++) {
          Robot r = new Robot(World.Instance().getGrid(), 0, i);
          bulkInsert.store(r, r.getUUID());
        }
      }

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      for (Robot r : session.query(Robot.class).toList()) {
        robots.put(r.getUUID(), r);
        World.Instance().RegisterRobot(r);
        World.Instance().addWall(r.getPx(), r.getPy());
      }
    }

    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          World.Instance().addWall((29 - x), y);
        }
      }
    }
  }

  public void update() {
    for (Map.Entry<String, Robot> r : robots.entrySet()) {
      r.getValue().update();
    }
  }

  private Node rackDropLocation() {
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 != 1 && y % 6 != 2 && y % 6 != 3 && y % 6 != 4) {
        continue;
      }
      for (int x : Data.rackPositionsX) {
        if (!World.Instance().getGrid().getNode(x, y).isOccupied()) {
          World.Instance().getGrid().getNode(x, y).updateOccupation(true);
          return World.Instance().getGrid().getNode(x, y);
        }
      }
    }
    return null;
  }

  @Override
  public void RequestResource(String resource, int amount) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Rack> pooledRacks = session.query(Rack.class)
        .whereEquals("status", Rack.RackStatus.STORED)
        .take(amount).toList();

      List<String> tmp = new ArrayList<>();
      for (Rack r : pooledRacks) {
        tmp.add(r.getUUID());
      }

      Waybill dump = new Waybill(new ArrayDeque<>(tmp), Destination.MELKFACTORY);
      WaybillResolver.Instance().StoreResource(dump);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void StoreResource(Waybill waybill) {
    Deque<String> cargo = waybill.getRacks();
    loadingBay++;
    if (loadingBay > 9) loadingBay = 0;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Robot> idleRobots = session.query(Robot.class)
        .whereEquals("status", Robot.RobotStatus.IDLE)
        .take(cargo.size()).toList();

      int i = 0;

      for (Robot r : idleRobots) {
        Robot robot = robots.get(r.getUUID());

        int x = truckpost[i][1] + 24,
          y = (truckpost[i][0] + (loadingBay * 6));

        Node delloc = World.Instance().getGrid().getNode(x, y);
        Node drop = rackDropLocation();

        i++;

        Rack rack = session.load(Rack.class, cargo.remove());
        rack.updatePosition(x, y, 0);

        World.Instance().updateObject(rack);

        LinkedList<RobotTask> t = new LinkedList<>();

        t.add(new RobotTask(delloc, RobotTask.Task.PICKUP));
        t.add(new RobotTask(drop, RobotTask.Task.DROP));
        t.add(new RobotTask(World.Instance().getGrid().getNode(robot.getPx(), robot.getPy()), RobotTask.Task.PARK));
        robot.assignTask(t);
        robot.setRack(rack);
        robot.setStatus(Robot.RobotStatus.WORKING);
        session.advanced().patch(robot.getUUID(), "status", Robot.RobotStatus.WORKING);
        //World.Instance().getGrid().getNode(drop.getGridX(), drop.getGridY()).updateOccupation(true);
      }
      session.saveChanges();
    }
  }

  void updateRack() {

  }
}
