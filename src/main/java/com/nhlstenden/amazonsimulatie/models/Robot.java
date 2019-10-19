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
  private Queue<RobotTask> taskQueue = new LinkedList<>();
  private RobotTask currentTask;
  private Deque<Node> pathToTask = new LinkedList<>();
  private String rackUUID;
  private String uuid;
  private RobotStatus status = RobotStatus.IDLE;

  private int x = 0;
  private int px = 0;
  private int y = 0;
  private int py = 0;
  private int z = 0;

  private int rotationX = 0;
  private int rotationY = 0;
  private int rotationZ = 0;

  public Robot() {
    this.uuid = UUID.randomUUID().toString();
    routingEngine = new RoutingEngine();
  }

  @JsonCreator
  public Robot(@JsonProperty("uuid")
                 String uuid,
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
    this();
    this.x = x;
    px = x;
    this.y = y;
    py = y;
  }

  public void registerGrid(Grid grid) {
    routingEngine = new RoutingEngine();
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

          try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
            Rack rack = session.load(Rack.class, rackUUID);
            rack.setStatus(Rack.RackStatus.MOVING);
            rack.updatePosition(x, y, -10);
            World.Instance().parentObject(uuid, rackUUID);
            session.saveChanges();
          }
          //grid.getNode(x, y).updateOccupation(true);
        } else if (currentTask.getTask() == RobotTask.Task.DROP) {
          try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
            Rack rack = session.load(Rack.class, rackUUID);
            rack.setStatus(Rack.RackStatus.STORED);
            rack.updatePosition(x, y, z);
            World.Instance().unparentObject(uuid, rackUUID);
            World.Instance().updateObject(rack);
            session.saveChanges();
          }
          //grid.getNode(x, y).updateOccupation(true);
        } else if (currentTask.getTask() == RobotTask.Task.PARK) {
          try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
            Robot robot = session.load(Robot.class, uuid);
            robot.setStatus(RobotStatus.IDLE);
            session.saveChanges();
          }
        }
      }

      World.Instance().updateObject(this);
    } else if (!taskQueue.isEmpty()) {
      executeNextTask();
    }
  }

  public void setStatus(RobotStatus s) {
      this.status = s;
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
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public int getZ() {
    return this.z;
  }

  @Override
  public int getRotationX() {
    return this.rotationX;
  }

  @Override
  public int getRotationY() {
    return this.rotationY;
  }

  @Override
  public int getRotationZ() {
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

  public void setRack(Rack rack) {
    this.rackUUID = rack.getUUID();
  }

  public enum RobotStatus {
    IDLE,
    WORKING
  }
}
