package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.base.RoutingEngine;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
public class Robot implements Object3D, Updatable, Poolable {
  private RoutingEngine routingEngine;
  private UUID uuid;
  private Grid grid;
  private ArrayList<RobotTask> taskQueue = new ArrayList<>();
  private RobotTask currentTask;
  private Deque<Node> pathToTask = new LinkedList<>();
  private Rack rack;

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

  public Robot(Grid grid, int x, int y) {
    this(grid);
    this.x = x;
    px = x;
    this.y = y;
    py = y;
  }

  public void assignTask(ArrayList<RobotTask> tasks) {
    taskQueue = tasks;
    executeNextTask();
  }

  private void executeNextTask() {
    currentTask = taskQueue.remove(0);
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
  @Override
  public boolean update() {
    if (!pathToTask.isEmpty()) {
      Node current = pathToTask.remove();
      this.x = current.getGridX();
      this.y = current.getGridY();
      if (current != currentTask.getDestination()) {
        return true;
      }
      if (currentTask.getTask() == RobotTask.Task.PICKUP){
        this.rack = (Rack) grid.getNode(x, y).getOccupation();
        grid.getNode(x, y).updateOccupation(this.rack);
        if(this.rack == null){
          taskQueue.clear();
          return true;
        }
        this.rack.updatePosition(-10);
        grid.getNode(x, y).updateOccupation(null);
      }
      else if(currentTask.getTask() == RobotTask.Task.DROP){
        if(this.rack == null){
          taskQueue.clear();
          return true;
        }
        this.rack.updatePosition(x, y);
      }
      // ipv new rack moet er een uit the obj pool komen.
      // dit rack wordt nog niet zichtbaar, alleen de navigatie node is geupdate.
      return true;
    }
    if (!taskQueue.isEmpty()) {
      executeNextTask();
    }
    return false;
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
    return this.z;
  }

  @Override
  public double getZ() {
    return this.y;
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

  @Override
  public void putInPool() {
  }

  public int getPx() {
    return px;
  }

  public int getPy() {
    return py;
  }
}
