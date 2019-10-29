package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.*;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.subscriptions.SubscriptionBatch;
import net.ravendb.client.documents.subscriptions.SubscriptionWorker;

import java.util.Queue;
import java.util.*;

import static com.nhlstenden.amazonsimulatie.models.Data.rackSpawnPositions;
import static com.nhlstenden.amazonsimulatie.models.generated.Waybill.Destination.MELKFACTORY;
import static com.nhlstenden.amazonsimulatie.models.generated.Waybill.Destination.WAREHOUSE;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class WarehouseManager implements Warehouse {
  private int loadingBay = 0;
  /*
   * De wereld bestaat uit objecten, vandaar de naam worldObjects. Dit is een lijst
   * van alle objecten in de 3D wereld. Deze objecten zijn in deze voorbeeldcode alleen
   * nog robots. Er zijn ook nog meer andere objecten die ook in de wereld voor kunnen komen.
   * Deze objecten moeten uiteindelijk ook in de lijst passen (overerving). Daarom is dit
   * een lijst van Object3D onderdelen. Deze kunnen in principe alles zijn. (Robots, vrachrtwagens, etc)
   */
  private HashMap<String, RobotLogic> robots = new HashMap<>();
  private Grid grid;
  /*
   * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
   * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
   */
  public WarehouseManager() {
    grid = new Grid(30, (6 * Data.modules));
    int rAmount = Data.modules * 6;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      rAmount -= session.query(Robot.class).count();

      if (rAmount > 0)
        try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
          for (int i = 0; i < rAmount; i++) {
            Robot rp = new Robot();
            rp.setStatus(Robot.Status.IDLE);
            rp.setX(0);
            rp.setY(i);
            bulkInsert.store(rp);

            RobotLogic r = new RobotLogic(session.advanced().getDocumentId(rp), grid, 0, i);
            r.registerWarehouse(this);
          }
        }

      for (Robot rp : session.query(Robot.class).toList()) {
        RobotLogic r = new RobotLogic(rp.getId(),grid, rp.getX(), rp.getY());
        r.registerWarehouse(this);
        robots.put(r.getId(), r);
        grid.addWall(rp.getX(), rp.getY());
      }
    }

    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          grid.addWall((29 - x), y);
        }
      }
    }

    String subscriptionName = DocumentStoreHolder.getStore().subscriptions().create(Waybill.class);
    SubscriptionWorker<Waybill> workerWBatch = DocumentStoreHolder.getStore().subscriptions().getSubscriptionWorker(Waybill.class, subscriptionName);
    workerWBatch.run(batch -> {
      for (SubscriptionBatch.Item<Waybill> item : batch.getItems()) {
        if (item.getResult().getStatus() == Waybill.Status.UNRESOLVED)
          RequestResource(item.getId());
      }
    });
  }

  public void update() {
    for (Map.Entry<String, RobotLogic> r : robots.entrySet()) {
      r.getValue().update();
    }
  }

  private Node rackDropLocation() {
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 != 1 && y % 6 != 2 && y % 6 != 3 && y % 6 != 4) {
        continue;
      }
      for (int x : Data.rackPositionsX) {
        if (!grid.getNode(x, y).isOccupied()) {
          grid.getNode(x, y).setOccupied(true);
          return grid.getNode(x, y);
        }
      }
    }
    return null;
  }

  private void RequestResource(String waybillId) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Waybill waybill = session.load(Waybill.class, waybillId);
      List<String> cargo = waybill.getRacks();
      waybill.setStatus(Waybill.Status.RESOLVING);
      waybill.setTodoList(cargo);
      loadingBay++;
      if (loadingBay > Data.modules - 1) loadingBay = 0;

      List<Robot> idleRobots = session.query(Robot.class)
        .whereEquals("status", Robot.Status.IDLE)
        .take(cargo.size()).toList();

      for (int i = 0; i < idleRobots.size(); i++) {
        Robot r = idleRobots.get(i);
        RobotLogic robotLogic = robots.get(r.getId());

        int x = rackSpawnPositions[i][1] + 24,
          y = (rackSpawnPositions[i][0] + (loadingBay * 6));

        Queue<RobotTaskStrategy> tasks = new LinkedList<>();
        Rack rack = session.load(Rack.class, cargo.get(i));
        if (waybill.getDestination() == WAREHOUSE){
          rack.setX(x);
          rack.setY(y);
          rack.setZ(0);

          MessageBroker.Instance().updateObject(rack);

          tasks.add(new RobotPickupStrategy(grid.getNode(x, y)));
          tasks.add(new RobotDropStrategy(rackDropLocation()));
        } else {
          tasks.add(new RobotPickupStrategy(grid.getNode(rack.getX(), rack.getY())));
          tasks.add(new RobotDropStrategy(grid.getNode(x, y)));
        }
        tasks.add(new RobotParkStrategy(grid.getNode(robotLogic.getPx(), robotLogic.getPy())));
        robotLogic.assignTask(tasks);

        robotLogic.setRack(rack);
        robotLogic.setWaybillUUID(waybill.getId());
        r.setStatus(Robot.Status.WORKING);
        r.setWaybill(waybill);
      }
      session.saveChanges();
    }
  }

  @Override
  public void robotFinishedTask(RobotLogic robotLogic, RobotTaskStrategy task) {
    if (task instanceof RobotDropStrategy) {
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        Waybill w = session.load(Waybill.class, robotLogic.getWaybillUUID());

        if (w.getTodoList() != null)
          w.getTodoList().remove(robotLogic.getRackUUID());

        if (w.getTodoList() == null || w.getTodoList().size() <= 0) {
          if (w.getDestination() == MELKFACTORY)
            for (String id : w.getRacks()) {
              Rack r = session.load(Rack.class, id);
              r.setZ(-10);
              r.setStatus(Rack.Status.POOLED);
              MessageBroker.Instance().updateObject(r);
            }
          session.delete(w);
        }

        session.saveChanges();
      }
    }
  }
}
