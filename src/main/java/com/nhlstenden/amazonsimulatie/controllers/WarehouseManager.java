package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Data;
import com.nhlstenden.amazonsimulatie.models.*;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.*;

public class WarehouseManager implements Resource {
  private int loadingBay = 0;
  private int time = 0;
  private HashMap<String, Robot> robots = new HashMap<>();
  private boolean[] loadingBayInUse = new boolean[Data.modules]; // for testing
  private Random r = new Random();

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
        if (World.Instance().getGrid().getNode(x, y).isOccupied())
          continue;
        World.Instance().getGrid().getNode(x, y).updateOccupation(true);
        return World.Instance().getGrid().getNode(x, y);
      }
    }
    return null;
  }

  @Override
  public void RequestResource(String resource, int amount) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Deque<Rack> temp = new ArrayDeque<>();
      for (int i = 0; i < 10; i++) {
        Rack r = session.query(Rack.class).whereEquals("status", Rack.RackStatus.STORED).first();
        temp.addFirst(r);
      }
      Waybill dump = new Waybill(temp);
      WaybillResolver.Instance().StoreResource(dump);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void StoreResource(Waybill waybill) {
    Deque<Rack> cargo = waybill.getRacks();
    loadingBay++;
    if (loadingBay > 9) loadingBay = 0;

    for (int y = (loadingBay * 6); y < ((loadingBay * 6) + 6); y++) {
      if (y % 6 != 2 && y % 6 != 3)
        continue;
      for (int x = 0; x <= 5; x++) {
        if (cargo.isEmpty())
          continue;

        Node delloc = World.Instance().getGrid().getNode((29 - x), y);
        Node drop = rackDropLocation();

        Rack rack = cargo.remove();
        World.Instance().addRack(rack.getType(), delloc.getGridX(), delloc.getGridY());

        Robot robot = findIdleRobot();
        if (robot == null)
          continue;
        LinkedList<RobotTask> t = new LinkedList<>();

        t.add(new RobotTask(delloc, RobotTask.Task.PICKUP));
        t.add(new RobotTask(drop, RobotTask.Task.DROP));
        t.add(new RobotTask(World.Instance().getGrid().getNode(0, r.nextInt((6 * Data.modules))), RobotTask.Task.PARK));
        robot.assignTask(t);
        robot.updateStatus(Robot.RobotStatus.WORKING);
        World.Instance().getGrid().getNode(drop.getGridX(), drop.getGridY()).updateOccupation(true);

        try {
          Thread.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private Robot findIdleRobot() {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Robot r = session.query(Robot.class)
        .whereEquals("status", Robot.RobotStatus.IDLE)
        .firstOrDefault();
      if (r == null) return null;
      return robots.get(r.getUUID());
    }
  }
}
