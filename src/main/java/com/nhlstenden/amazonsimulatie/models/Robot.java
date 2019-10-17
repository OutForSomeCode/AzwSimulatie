package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhlstenden.amazonsimulatie.base.RoutingEngine;
import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
public class Robot implements Object3D, Poolable {
  private RoutingEngine routingEngine;
  private Grid grid;
  private Queue<RobotTask> taskQueue = new LinkedList<>();
  private RobotTask currentTask;
  private Deque<Node> pathToTask = new LinkedList<>();
  private Rack rack;
  private UUID uuid;
  private RobotStatus status = RobotStatus.IDLE;
  private int x;
  private int y;
  private int z;
  private int rotationX;
  private int rotationY;
  private int rotationZ;

  private int x = 0;
  private int px = 0;
  private int y = 0;
  private int py = 0;
  private int z = 0;

  private int rotationX = 0;
  private int rotationY = 0;
  private int rotationZ = 0;

  public Robot(Grid grid) {
    this.uuid = UUID.randomUUID();
    this.grid = grid;
    routingEngine = new RoutingEngine(grid);
  }

  @JsonCreator
  public Robot(@JsonProperty("uuid")
                 UUID uuid,
               @JsonProperty("status")
                 RobotStatus status,
               @JsonProperty("x")
                 int x,
               @JsonProperty("y")
                 int y,
               @JsonProperty("z")
                 int z,
               @JsonProperty("rotationX")
                 int rotationX,
               @JsonProperty("rotationY")
                 int rotationY,
               @JsonProperty("rotationZ")
                 int rotationZ) {
    this.uuid = uuid;
    this.status = status;

    this.x = x;
    this.y = y;
    this.z = z;

    this.rotationX = rotationX;
    this.rotationY = rotationY;
    this.rotationZ = rotationZ;
  }

  public Robot(Grid grid, int x, int y) {
    this(grid);
    this.x = x;
    px = x;
    this.y = y;
    py = y;
  }

  public void registerGrid(Grid grid) {
    this.grid = grid;
    routingEngine = new RoutingEngine(grid);
  }

  public void assignTask(LinkedList<RobotTask> tasks) {
    taskQueue = tasks;
    executeNextTask();
  }

  private void executeNextTask() {
    currentTask = taskQueue.remove();
    pathToTask = routingEngine.generateRoute(new Node(x, y), currentTask.getDestination());
  }

  /*
   * Deze update methode wordt door de World aangeroepen wanneer de
   * World zelf geupdate wordt. Dit betekent dat elk object, ook deze
   * robot, in de 3D wereld steeds een beetje tijd krijgt om een update
   * uit te voeren. In de updatemethode hieronder schrijf je dus de code
   * die de robot steeds uitvoert (bijvoorbeeld positieveranderingen). Wanneer
   * de methode true teruggeeft (zoals in het voorbeeld), betekent dit dat
   * er inderdaad iets veranderd is en dat deze nieuwe informatie naar de views
   * moet worden gestuurd. Wordt false teruggegeven, dan betekent dit dat er niks
   * is veranderd, en de informatie hoeft dus niet naar de views te worden gestuurd.
   * (Omdat de informatie niet veranderd is, is deze dus ook nog steeds hetzelfde als
   * in de view)
   */
  public void update() {
    if (!pathToTask.isEmpty()) {
      Node current = pathToTask.remove();
      this.x = current.getGridX();
      this.y = current.getGridY();
      if (current == currentTask.getDestination()) {
        if (currentTask.getTask() == RobotTask.Task.PICKUP) {
          updatePosition();
          try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
            this.rack = session.query(Rack.class)
              .whereEquals("x", x)
              .andAlso()
              .whereEquals("y", y)
              .first();
          }

          this.rack.updateStatus(Rack.RackStatus.MOVING);
          this.rack.updatePosition(currentTask.getDestination().getGridX(), currentTask.getDestination().getGridX(), -10);
          grid.getWorld().updateObject(this.rack);

          //grid.getNode(x, y).updateOccupation(true);
        } else if (currentTask.getTask() == RobotTask.Task.DROP) {
          updatePosition();
          try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
            session.advanced().patch(rack.getUUID().toString(), "status", Rack.RackStatus.STORED);
            session.saveChanges();
          }
          this.rack.updatePosition(x, y, z);
          grid.getWorld().updateObject(this.rack);
          //grid.getNode(x, y).updateOccupation(true);
        } else if (currentTask.getTask() == RobotTask.Task.PARK) {
          updatePosition();
          updateStatus(RobotStatus.IDLE);
        }
        this.rack.updatePosition(x, y);
      }

      World.Instance().updateObject(this);
    }
    else if (!taskQueue.isEmpty()) {
      executeNextTask();
    }
  }

  public void updateStatus(RobotStatus s) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      this.status = s;
      session.advanced().patch(uuid.toString(), "status", s);
      session.saveChanges();
    }
  }

  void updatePosition() {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Robot r = session.load(Robot.class, uuid.toString());
      r.x = this.x;
      r.y = this.y;
      r.z = this.z;
      session.saveChanges();
    }
    World.Instance().updateObject(this);
  }

  @Override
  public boolean inUse() {
    return !taskQueue.isEmpty();
  }

  @Override
  public String getUUID() {
    return this.uuid.toString();
  }

  @Override
  public String getType() {
    /*
     * Dit onderdeel wordt gebruikt om het type van dit object als stringwaarde terug
     * te kunnen geven. Het moet een stringwaarde zijn omdat deze informatie nodig
     * is op de client, en die verstuurd moet kunnen worden naar de browser. In de
     * javascript code wordt dit dan weer verder afgehandeld.
     */
    return Robot.class.getSimpleName().toLowerCase();
  }

  @Override
  public double getX() {
    return this.x;
  }

  @Override
  public double getY() {
    return this.y;
  }

  @Override
  public double getZ() {
    return this.z;
  }

  @Override
  public double getRotationX() {
    return this.rotationX;
  }

  @Override
  public double getRotationY() {
    return this.rotationY;
  }

  @Override
  public double getRotationZ() {
    return this.rotationZ;
  }

  public RobotStatus getStatus() {
    return status;
  }

  @Override
  public void putInPool() {
  }

  public int getPx() {
    return px;
  }

  public int getPy() {
    return py;
  }

  public enum RobotStatus {
    IDLE,
    WORKING
  }
}
