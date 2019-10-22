package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.controllers.RoutingEngine;

import java.util.*;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
public class Robot implements Object3D, Poolable {
  private RoutingEngine routingEngine;
  private Queue<RobotTaskStrategy> taskQueue = new LinkedList<>();
  private RobotTaskStrategy currentTask;
  private Deque<Node> pathToTask = new LinkedList<>();
  private String rackUUID;
  private String uuid;
  private boolean executeTask = false;

  private int x = 0;
  private int px = 0;
  private int y = 0;
  private int py = 0;
  private int z = 0;

  private int rotationX = 0;
  private int rotationY = 0;
  private int rotationZ = 0;

  public Robot(String uuid) {
    this.uuid = uuid;
    routingEngine = new RoutingEngine();
  }

  public Robot() {
    this.uuid = UUID.randomUUID().toString();
    routingEngine = new RoutingEngine();
  }

  public Robot(int x, int y) {
    this();
    this.x = x;
    px = x;
    this.y = y;
    py = y;
  }

  public Robot(String uuid, Integer x, Integer y) {
    this(uuid);
    this.x = x;
    px = x;
    this.y = y;
    py = y;
  }

  public void assignTask(Queue<RobotTaskStrategy> tasks) {
    taskQueue = tasks;
    executeNextTask();
  }

  private void executeNextTask() {
    currentTask = taskQueue.remove();
    pathToTask = routingEngine.generateRoute(new Node(x, y), currentTask.getDestination());
  }

  public void taskDone() {

  }

  public String getRackUUID() {
    return rackUUID;
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
    if (executeTask) {
      executeTask = false;
      currentTask.execute(this);
    } else if (!pathToTask.isEmpty()) {
      Node current = pathToTask.remove();
      this.x = current.getGridX();
      this.y = current.getGridY();
      if (current == currentTask.getDestination()) {
        executeTask = true;
      }

      MessageBroker.Instance().updateObject(this);
    } else if (!taskQueue.isEmpty()) {
      executeNextTask();
    }
  }

  @Override
  public boolean inUse() {
    return !taskQueue.isEmpty();
  }

  @Override
  public String getUUID() {
    return this.uuid;
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
