package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.*;
import com.nhlstenden.amazonsimulatie.models.generated.*;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.indexes.spatial.SpatialRelation;
import net.ravendb.client.documents.queries.spatial.WktField;
import net.ravendb.client.documents.session.IDocumentSession;
import net.ravendb.client.documents.subscriptions.SubscriptionBatch;
import net.ravendb.client.documents.subscriptions.SubscriptionWorker;

import java.util.Queue;
import java.util.*;

import static com.nhlstenden.amazonsimulatie.models.Data.rackSpawnPositions;
import static com.nhlstenden.amazonsimulatie.models.generated.Waybill.Destination.MILKFACTORY;
import static com.nhlstenden.amazonsimulatie.models.generated.Waybill.Destination.WAREHOUSE;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class WarehouseManager implements Warehouse {
  CargoCrane cargoCrane;
  private boolean[] loadingBays;
  private HashMap<String, RobotLogic> robots = new HashMap<>();
  private Grid grid;

  public WarehouseManager() {
    grid = new Grid(Data.moduleLength, (6 * Data.modules));
    int numberOfRobots = Data.modules * 10;
    numberOfRobots -= (int) Math.ceil(Data.modules / 5.0) * 10;

    cargoCrane = new CargoCrane(this);
    loadingBays = new boolean[Data.modules];

    // open connection to the database
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      //check how many robots are already in the database
      numberOfRobots -= session.query(Robot.class).count();

      // add the robots needed to the database
      if (numberOfRobots > 0)
        try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
          for (int m = 0; m < Data.modules; m++) {
            if (m % 5 == 2) {
              for (int y : Data.rackPositionsY) {
                for (int x : Data.rackPositionsX) {
                  if (numberOfRobots > 0) {
                    Robot robot = new Robot();
                    robot.setStatus(Robot.Status.IDLE);
                    robot.setX(x);
                    int yf = y + (m * 6);
                    robot.setY(yf);

                    robot.setWkt(DocumentStoreHolder.formatWtk(x, yf));

                    bulkInsert.store(robot);

                    numberOfRobots -= 1;
                  }
                }
              }
            }
          }
        }

      // give the robot its logic and assign the warehouse
      for (Robot robot : session.query(Robot.class).toList()) {
        RobotLogic logic = new RobotLogic(robot.getId(), grid, robot.getX(), robot.getY());
        logic.registerWarehouse(this);
        robots.put(logic.getId(), logic);
        grid.addWall(robot.getX(), robot.getY());
      }

      // dont remove! is not used but without the database will give a error ....
      List<Robot> results = session
        .query(Robot.class)
        .spatial(
          new WktField("wkt"),
          f -> f.relatesToShape("Circle(53.000000 6.000000 d=10.0000)", SpatialRelation.WITHIN)
        )
        .toList();
    }

    // put invisible walls around the loading bays so the robots cant go there
    for (int y = 0; y < (6 * Data.modules); y++) {
      if (y % 6 == 0 || y % 6 == 1 || y % 6 == 4 || y % 6 == 5) {
        for (int x = 0; x < 6; x++) {
          grid.addWall((29 - x), y);
        }
      }
    }

    // get the waybills from the database that have not yet been resolved and create a cargo container for them
    String subscriptionName = DocumentStoreHolder.getStore().subscriptions().create(Waybill.class);
    SubscriptionWorker<Waybill> workerWBatch = DocumentStoreHolder.getStore().subscriptions().getSubscriptionWorker(Waybill.class, subscriptionName);
    workerWBatch.run(batch -> {
      for (SubscriptionBatch.Item<Waybill> item : batch.getItems()) {
        if (item.getResult().getStatus() == Waybill.Status.UNRESOLVED) {
          if (item.getResult().getDestination().equals(WAREHOUSE))
            cargoCrane.addContainer(CargoCrane.ContainerData.Task.DROP, item.getId(), assignLoadingBay());
          else
            cargoCrane.addContainer(CargoCrane.ContainerData.Task.PICKUP, item.getId(), assignLoadingBay());
        }
      }
    });
  }

  // execute the update commands for robots and the cargo crane
  public void update() {
    for (Map.Entry<String, RobotLogic> logicEntry : robots.entrySet()) {
      logicEntry.getValue().update();
    }
    cargoCrane.update();
  }

  // give back an available node to drop a rack
  private Node rackDropLocation() {
    for (int m = 0; m < Data.modules; m++) {
      if (m % 5 != 2) {
        for (int y : Data.rackPositionsY) {
          for (int x : Data.rackPositionsX) {
            if (!grid.getNode(x, (m * 6) + y).isOccupied()) {
              grid.getNode(x, (m * 6) + y).setOccupied(true);
              return grid.getNode(x, (m * 6) + y);
            }
          }
        }
      }
    }
    return null;
  }

  // get an available loading bay
  private int assignLoadingBay() {
    int i = 0;

    while (loadingBays[i]){
      i++;
      if(i >= loadingBays.length)
        break;
    }


    loadingBays[i] = true;
    return i;
  }

  // after the cargo crane has set the container in a loading bay the corresponding waybill will be handled
  public void processWaybill(String waybillId, int containerPosY) {
    int containerPosX = 24;

    // open a connection to the database
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {

      // get the given waybill
      Waybill waybill = session.load(Waybill.class, waybillId);
      List<String> cargo = new ArrayList<>();//waybill.getRacks();

      waybill.setX(containerPosX);
      waybill.setY(containerPosY);
      waybill.setZ(0);

      MessageBroker.Instance().updateObject(waybill);

      int numberOfRacks = waybill.getRacksAmount();
      List<Rack> racks;

      // get racks that are not in use (not visible in the warehouse)
      if (waybill.getDestination() == WAREHOUSE) {
        racks = session.query(Rack.class)
          .whereEquals("status", Rack.Status.POOLED)
          .take(numberOfRacks).toList();

        if (racks.size() <= numberOfRacks) {
          numberOfRacks -= racks.size();

          // create more racks if there are not enough in the database
          try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
            for (int i = 0; i < numberOfRacks; i++) {
              Rack rack = new Rack();
              rack.setItem("kaas");
              rack.setWkt(DocumentStoreHolder.formatWtk(containerPosX, containerPosY));
              rack.setStatus(Rack.Status.WAITING);
              bulkInsert.store(rack);
              racks.add(rack);
            }
          }

          // dont remove! is not used but without the database will give a error ....
          List<Rack> results = session
            .query(Rack.class)
            .spatial(
              new WktField("wkt"),
              f -> f.relatesToShape("Circle(53.000000 6.000000 d=10.0000)", SpatialRelation.WITHIN)
            )
            .toList();
        }
      } else {
        // get racks from the warehouse closest to where there needed
        racks = session.query(Rack.class)
          .whereEquals("status", Rack.Status.STORED)
          .orderByDistance("wkt", DocumentStoreHolder.formatWtk(containerPosX, containerPosY))
          .take(numberOfRacks).toList();
      }

      for (Rack rack : racks) {
        cargo.add(rack.getId());
      }

      waybill.setStatus(Waybill.Status.RESOLVING);
      waybill.setTodoList(cargo);
      waybill.getRacks().clear();

      // get idle robots to transport racks
      List<Robot> idleRobots = session.query(Robot.class)
        .whereEquals("status", Robot.Status.IDLE)
        .orderByDistance("wkt", DocumentStoreHolder.formatWtk(containerPosX, containerPosY))
        .take(cargo.size()).toList();

      for (int i = 0; i < idleRobots.size(); i++) {
        Robot idleRobot = idleRobots.get(i);
        RobotLogic robotLogic = robots.get(idleRobot.getId());

        int x = rackSpawnPositions[i][1] + containerPosX,
          y = (rackSpawnPositions[i][0] + containerPosY);

        // create a queue with tasks for the robot to execute
        Queue<RobotTaskStrategy> tasks = new LinkedList<>();
        Rack rack = racks.get(i);//session.load(Rack.class, );//cargo.get(i)
        if (waybill.getDestination() == WAREHOUSE) {
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
        idleRobot.setStatus(Robot.Status.WORKING);
        idleRobot.setWaybill(waybill);
      }
      session.saveChanges();
    }
  }

  // callback for when the robot has finished a task
  @Override
  public void robotFinishedTask(RobotLogic robotLogic, RobotTaskStrategy task) {
    if (task instanceof RobotDropStrategy) {
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        Waybill waybill = session.load(Waybill.class, robotLogic.getWaybillUUID());

        if (waybill.getTodoList() != null) {
          waybill.getTodoList().remove(robotLogic.getRackUUID());
          waybill.getRacks().add(robotLogic.getRackUUID());
        }

        // check if all racks that where requested where delivered
        if (waybill.getTodoList() == null || waybill.getTodoList().size() <= 0) {
          if (waybill.getDestination() == MILKFACTORY) {
            for (String id : waybill.getRacks()) {
              Rack rack = session.load(Rack.class, id);
              rack.setZ(-10);
              rack.setStatus(Rack.Status.POOLED);
              MessageBroker.Instance().updateObject(rack);
            }
          }

          waybill.setZ(-50);
          MessageBroker.Instance().updateObject(waybill);

          // give the loading bay free and instead of removing the used waybill we recycle them in the database
          loadingBays[waybill.getLoadingBay()] = false;
          waybill.setStatus(Waybill.Status.POOLED);
        }
        session.saveChanges();
      }
    }
  }
}
