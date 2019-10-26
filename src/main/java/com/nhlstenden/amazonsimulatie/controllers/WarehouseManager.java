package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.*;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.Queue;
import java.util.*;

import static com.nhlstenden.amazonsimulatie.models.Data.truckpost;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class WarehouseManager implements Resource, Warehouse {
  private int loadingBay = 0;
  private int timer = 0;
  /*
   * De wereld bestaat uit objecten, vandaar de naam worldObjects. Dit is een lijst
   * van alle objecten in de 3D wereld. Deze objecten zijn in deze voorbeeldcode alleen
   * nog robots. Er zijn ook nog meer andere objecten die ook in de wereld voor kunnen komen.
   * Deze objecten moeten uiteindelijk ook in de lijst passen (overerving). Daarom is dit
   * een lijst van Object3D onderdelen. Deze kunnen in principe alles zijn. (Robots, vrachrtwagens, etc)
   */
  private HashMap<String, RobotImp> robots = new HashMap<>();
  private boolean[] loadingBayInUse = new boolean[Data.modules]; // for testing
  private Random rand = new Random();

  private List<String> waybills = new ArrayList<>();

  /*
   * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
   * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
   */
  public WarehouseManager() {
    int rAmount = Data.modules * 6;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      rAmount -= session.query(Robot.class).count();

      if (rAmount > 0)
        try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
          for (int i = 0; i < rAmount; i++) {
            Robot rp = new Robot();
            rp.setX(0);
            rp.setY(i);
            bulkInsert.store(rp);

            RobotImp r = new RobotImp(session.advanced().getDocumentId(rp), 0, i);
            r.registerWarehouse(this);
          }
        }

      for (Robot rp : session.query(Robot.class).toList()) {
        RobotImp r = new RobotImp(rp.getId(), rp.getX(), rp.getY());
        r.registerWarehouse(this);
        robots.put(r.getId(), r);
        MessageBroker.Instance().addWall(rp.getX(), rp.getY());
      }
    }

    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          MessageBroker.Instance().addWall((29 - x), y);
        }
      }
    }
  }

  public void update() {
    for (Map.Entry<String, RobotImp> r : robots.entrySet()) {
      r.getValue().update();
    }
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Waybill> uway = session.query(Waybill.class)
        .whereEquals("status", Waybill.Status.UNRESOLVED)
        .andAlso()
        .whereEquals("destination", Waybill.Destination.WAREHOUSE)
        .toList();
      if (uway.size() <= 0)
        return;
      for (Waybill w : uway) {
        w.setStatus(Waybill.Status.RESOLVING);
        StoreResource(w);
      }

      session.saveChanges();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private Node rackDropLocation() {
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 != 1 && y % 6 != 2 && y % 6 != 3 && y % 6 != 4) {
        continue;
      }
      for (int x : Data.rackPositionsX) {
        if (!MessageBroker.Instance().getGrid().getNode(x, y).isOccupied()) {
          MessageBroker.Instance().getGrid().getNode(x, y).updateOccupation(true);
          return MessageBroker.Instance().getGrid().getNode(x, y);
        }
      }
    }
    return null;
  }

  @Override
  public void RequestResource(String resource, int amount) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Rack> pooledRacks = session.query(Rack.class)
        .whereEquals("status", Rack.Status.STORED)
        .take(amount).toList();

      List<String> tmp = new ArrayList<>();
      for (Rack r : pooledRacks)
        tmp.add(r.getId());

      Waybill dump = new Waybill();
      dump.setRacks(tmp);
      dump.setDestination(Waybill.Destination.MELKFACTORY);
      //WaybillResolver.Instance().StoreResource(dump.getUuid(), Destination.MELKFACTORY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void StoreResource(Waybill waybill) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<String> cargo = waybill.getRacks();
      loadingBay++;
      if (loadingBay > 9) loadingBay = 0;

      List<Robot> idleRobots = session.query(Robot.class)
        .whereEquals("status", Robot.Status.IDLE)
        .take(cargo.size()).toList();

      for (int i = 0; i < idleRobots.size(); i++) {
        Robot r = idleRobots.get(i);
        RobotImp robotImp = robots.get(r.getId());

        int x = truckpost[i][1] + 24,
          y = (truckpost[i][0] + (loadingBay * 6));

        Rack rack = session.load(Rack.class, cargo.get(i));
        //rack.updatePosition(x, y, 0);
        rack.setX(x);
        rack.setY(y);
        rack.setZ(0);

        MessageBroker.Instance().updateObject(rack);

        Queue<RobotTaskStrategy> tasks = new LinkedList<>();
        tasks.add(new RobotPickupStrategy(MessageBroker.Instance().getGrid().getNode(x, y)));
        tasks.add(new RobotDropStrategy(rackDropLocation()));
        tasks.add(new RobotParkStrategy(MessageBroker.Instance().getGrid().getNode(robotImp.getPx(), robotImp.getPy())));
        robotImp.assignTask(tasks);

        robotImp.setRack(rack);
        robotImp.setWaybillUUID(waybill.getId());
        r.setStatus(Robot.Status.WORKING);
        r.setWaybill(waybill);
      }
      //session.delete(waybill.getId());
      session.saveChanges();
    }
  }

  @Override
  public void robotFinishedTask(RobotImp robotImp, RobotTaskStrategy task) {
    if (task instanceof RobotDropStrategy) {
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        Waybill w = session.load(Waybill.class, robotImp.getWaybillUUID());
        Rack r = session.load(Rack.class, robotImp.getRackUUID());

        if (w.getRacks() != null)
          w.getRacks().remove(r.getId());

        if (w.getRacks() == null || w.getRacks().size() <= 0)
          session.delete(w);

        session.saveChanges();
      }
    }
  }
}
