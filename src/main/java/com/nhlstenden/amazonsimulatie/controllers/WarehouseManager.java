package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Data;
import com.nhlstenden.amazonsimulatie.models.*;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.*;

public class WarehouseManager implements Resource {
  private int loadingBay = 0;
  private World world;
  private int time = 0;
  private HashMap<String, Robot> robots = new HashMap<>();
  private boolean[] loadingBayInUse = new boolean[Data.modules]; // for testing
  private Random r = new Random();

  public WarehouseManager(World world) {
    this.world = world;

    int rAmount = Data.modules;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      rAmount -= session.query(Robot.class).count();
    }

    if (rAmount > 0)
      try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
        for (int i = 0; i < rAmount; i++) {
          Robot r = new Robot(world.getGrid(), 0, i);
          bulkInsert.store(r, r.getUUID());
        }
      }

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      for (Robot r : session.query(Robot.class).toList()) {
        robots.put(r.getUUID(), r);
        world.RegisterRobot(r);
      }
    }

    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          world.addWall((29 - x), y);
        }
      }
    }
  }

  public void update() {
  }

  private Node rackDropLocation() {
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 != 1 && y % 6 != 2 && y % 6 != 3 && y % 6 != 4) {
        continue;
      }
      for (int x : Data.rackPositionsX) {
        if (world.getGrid().getNode(x, y).getOccupation() != null)
          continue;
        world.getGrid().getNode(x, y).updateOccupation(new Wall());
        return world.getGrid().getNode(x, y);
      }
    }
    return null;
  }

  @Override
  public void RequestResource(String resource, int amount) {
    Deque<Rack> temp = new ArrayDeque<>();
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      for (int i = 0; i < 10; i++) {
        Rack r = session.query(Rack.class).whereEquals("status", Rack.RackStatus.STORED).single();
        temp.addFirst(r);
      }
    }
    Waybill dump = new Waybill(temp);
    WaybillResolver.Instance().StoreResource(dump);
  }

  @Override
  public void StoreResource(Waybill waybill) {
    Deque<Rack> cargo = waybill.getRacks();
    loadingBay++;
    if (loadingBay > 9) loadingBay = 0;

    for (int y = (loadingBay * 6); y < ((loadingBay * 6) + 6); y++) {
      if (y % 6 != 2 && y % 6 != 3)
        continue;
      for (int x = 0; x < 5; x++) {
        if (cargo.isEmpty())
          continue;

        Rack rack = cargo.remove();
        world.addRack(rack.getType(), 29 - x, y);

        Robot robot = findIdleRobot();
        if (robot == null)
          continue;
        ArrayList<RobotTask> t = new ArrayList<>();
        t.add(new RobotTask(world.getGrid().getNode((29 - x), y), RobotTask.Task.PICKUP));
        t.add(new RobotTask(rackDropLocation(), RobotTask.Task.DROP));
        t.add(new RobotTask(world.getGrid().getNode(robot.getPx(), robot.getPy()), RobotTask.Task.PARK));
        robot.assignTask(t);
        robot.updateStatus(Robot.RobotStatus.WORKING);
      }
    }
  }

  private Robot findIdleRobot() {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Robot r = session.query(Robot.class)
        .whereEquals("status", Robot.RobotStatus.IDLE)
        .firstOrDefault();
      if(r == null) return null;
      return robots.get(r.getUUID());
    }
  }
}
