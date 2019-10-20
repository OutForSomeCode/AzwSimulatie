package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Destination;
import com.nhlstenden.amazonsimulatie.models.*;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.*;

import static com.nhlstenden.amazonsimulatie.models.Data.truckpost;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class WarehouseManager implements Resource {
  private int loadingBay = 0;
  private int time = 0;
  /*
   * De wereld bestaat uit objecten, vandaar de naam worldObjects. Dit is een lijst
   * van alle objecten in de 3D wereld. Deze objecten zijn in deze voorbeeldcode alleen
   * nog robots. Er zijn ook nog meer andere objecten die ook in de wereld voor kunnen komen.
   * Deze objecten moeten uiteindelijk ook in de lijst passen (overerving). Daarom is dit
   * een lijst van Object3D onderdelen. Deze kunnen in principe alles zijn. (Robots, vrachrtwagens, etc)
   */
  private HashMap<String, Robot> robots = new HashMap<>();
  private boolean[] loadingBayInUse = new boolean[Data.modules]; // for testing
  private Random rand = new Random();

  /*
   * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
   * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
   */
  public WarehouseManager() {
    int rAmount = Data.modules * 6;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      rAmount -= session.query(RobotPOJO.class).count();

      if (rAmount > 0)
        try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
          for (int i = 0; i < rAmount; i++) {
            Robot r = new Robot(0, i);
            RobotPOJO rp = new RobotPOJO(r.getUUID(), 0, i);
            bulkInsert.store(rp, r.getUUID());
          }
        }

      for (RobotPOJO rp : session.query(RobotPOJO.class).toList()) {
        Robot r = new Robot(rp.getUUID(), rp.getX(), rp.getY());
        robots.put(r.getUUID(), r);
        MessageBroker.Instance().addWall(r.getPx(), r.getPy());
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
      List<RobotPOJO> idleRobots = session.query(RobotPOJO.class)
        .whereEquals("status", Robot.RobotStatus.IDLE)
        .take(cargo.size()).toList();

      int i = 0;

      for (RobotPOJO r : idleRobots) {
        Robot robot = robots.get(r.getUUID());

        int x = truckpost[i][1] + 24,
          y = (truckpost[i][0] + (loadingBay * 6));

        Node delloc = MessageBroker.Instance().getGrid().getNode(x, y);
        Node drop = rackDropLocation();

        i++;

        Rack rack = session.load(Rack.class, cargo.remove());
        rack.updatePosition(x, y, 0);

        MessageBroker.Instance().updateObject(rack);

        LinkedList<RobotTask> t = new LinkedList<>();

        t.add(new RobotTask(delloc, RobotTask.Task.PICKUP));
        t.add(new RobotTask(drop, RobotTask.Task.DROP));
        t.add(new RobotTask(MessageBroker.Instance().getGrid().getNode(robot.getPx(), robot.getPy()), RobotTask.Task.PARK));
        robot.assignTask(t);
        robot.setRack(rack);
        session.advanced().patch(robot.getUUID(), "status", Robot.RobotStatus.WORKING);
      }
      session.saveChanges();
    }
  }
}
