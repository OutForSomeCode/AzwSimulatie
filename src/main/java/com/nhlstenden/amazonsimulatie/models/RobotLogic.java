package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.controllers.RoutingEngine;
import com.nhlstenden.amazonsimulatie.controllers.Warehouse;
import com.nhlstenden.amazonsimulatie.models.generated.Object3D;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
public class RobotLogic extends Object3D {
  private RoutingEngine routingEngine;
  private Queue<RobotTaskStrategy> taskQueue = new LinkedList<>();
  private RobotTaskStrategy currentTask;
  private Deque<Node> pathToTask = new LinkedList<>();
  private String rackUUID;
  private String waybillUUID;
  private Warehouse warehouse;
  private boolean executeTask = false;
  private int px = 0;
  private int py = 0;

  public RobotLogic(String uuid) {
    this.setId(uuid);
    routingEngine = new RoutingEngine();
  }

  public RobotLogic(String uuid, Integer x, Integer y) {
    this(uuid);
    this.setX(x);
    px = x;
    this.setY(y);
    py = y;
  }

  public String getWaybillUUID() {
    return waybillUUID;
  }

  public void setWaybillUUID(String waybillUUID) {
    this.waybillUUID = waybillUUID;
  }

  public void registerWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public void assignTask(Queue<RobotTaskStrategy> tasks) {
    taskQueue = tasks;
  }

  private void executeNextTask() {
    currentTask = taskQueue.remove();
    pathToTask = routingEngine.generateRoute(new Node(getX(), getY()), currentTask.getDestination());
  }

  public void taskDone(RobotTaskStrategy task) {
    warehouse.robotFinishedTask(this, task);
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
      this.setX(current.getGridX());
      this.setY(current.getGridY());
      if (current == currentTask.getDestination()) {
        executeTask = true;
      }

      MessageBroker.Instance().updateObject(this);
    } else if (!taskQueue.isEmpty()) {
      executeNextTask();
    }
  }

  public int getPx() {
    return px;
  }

  public int getPy() {
    return py;
  }

  public void setRack(Rack rack) {
    this.rackUUID = rack.getId();
  }
}
